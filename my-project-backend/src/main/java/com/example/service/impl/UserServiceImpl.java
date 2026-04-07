package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.SyncUserDTO;
import com.example.entity.po.User;
import com.example.entity.vo.SyncUserVO;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.utils.JwtUtils;
import com.example.utils.SignUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder; // 必须导入
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private PasswordEncoder passwordEncoder; // 注入密码加密器

    @Value("${fanya.api.static-key}")
    private String staticKey;

    @Override
    public User findUserByUsername(String username) {
        return this.query()
                .eq("username", username)
                .one();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.findUserByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        String roleName = (user.getRole() != null && user.getRole() == 2) ? "TEACHER" : "STUDENT";
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .roles(roleName)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SyncUserVO syncUser(SyncUserDTO dto) {
        // 1. 签名校验
        SignUtils.verifyPlatformSignature(dto, staticKey);

        SyncUserDTO.UserInfoDTO info = dto.getUserInfo();

        // 2. 幂等检查
        User existing = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPlatformId, dto.getPlatformId())
                .eq(User::getPlatformUserId, info.getUserId()));

        Long internalId;
        Integer roleInt = "teacher".equalsIgnoreCase(info.getRole()) ? 2 : 1;

        if (existing != null) {
            log.info("同步更新现有用户: {}", info.getUserName());
            existing.setRole(roleInt);
            updateUserProperties(existing, info);
            userMapper.updateById(existing);
            internalId = existing.getId();
        } else {
            log.info("同步创建新用户: {}", info.getUserName());
            User newUser = new User();
            newUser.setPlatformId(dto.getPlatformId());
            newUser.setPlatformUserId(info.getUserId());
            newUser.setRole(roleInt);

            // ======= 核心修复点：使用 passwordEncoder 进行加密存储 =======
            // 为同步用户设置一个默认密码（例如 123456），并进行 BCrypt 加密
            // 这样登录接口调用 passwordEncoder.matches 时才能匹配成功
            String rawPassword = "fanya" + info.getUserId(); // 也可以设置为固定 123456
            newUser.setPassword(passwordEncoder.encode(rawPassword));
            // =========================================================

            newUser.setCreateTime(LocalDateTime.now());
            updateUserProperties(newUser, info);
            userMapper.insert(newUser);
            internalId = newUser.getId();
        }

        // 3. 生成认证信息
        String roleName = (roleInt == 2) ? "TEACHER" : "STUDENT";

        // 使用外部ID作为账号构建 UserDetails
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(info.getUserId())
                .password("") // 此处构建UserDetails仅用于JWT签发，密码不重要
                .roles(roleName)
                .build();

        // 签发 JWT
        String token = jwtUtils.createJwt(userDetails, info.getUserId(), internalId);

        return SyncUserVO.builder()
                .internalUserId(String.valueOf(internalId))
                .syncStatus("success")
                .authToken(token)
                .build();
    }

    private void updateUserProperties(User user, SyncUserDTO.UserInfoDTO info) {
        user.setRealName(info.getUserName());
        user.setUsername(info.getUserId()); // 账号存为学号/工号
        user.setSchoolId(info.getSchoolId());
        if (info.getContactInfo() != null) {
            user.setPhone(info.getContactInfo().getPhone());
            user.setEmail(info.getContactInfo().getEmail());
        }
    }
}