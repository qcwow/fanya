package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.entity.RestBean;
import com.example.entity.dto.LoginDTO;
import com.example.entity.po.User;
import com.example.entity.vo.UserVO;
import com.example.service.UserService;
import com.example.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "用户登录", description = "用户登录退出相关接口")
public class UserController {

    @Resource
    JwtUtils utils;

    @Resource
    UserService userService;

    @Resource
    PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "登录验证接口")
    public RestBean<UserVO> login(@RequestBody LoginDTO loginDTO) {
        // 1. 查询数据库中的用户信息
        User user = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, loginDTO.getUsername()));

        // 2. 验证用户是否存在以及密码是否正确 (使用 matches 校验 BCrypt)
        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return RestBean.failure(401, "用户名或密码错误");
        }

        // 3. 处理角色转换
        // 约定：1 为学生，2 为老师
        boolean isTeacher = Integer.valueOf(2).equals(user.getRole());
        String roleStr = isTeacher ? "TEACHER" : "STUDENT";
        String roleName = isTeacher ? "老师" : "学生";

        // 4. 构建 Spring Security 的 UserDetails 给 JWT 工具类使用
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(roleStr) // 内部权限系统使用字符串标识
                .build();

        // 5. 生成 JWT Token (确保 JwtUtils.createJwt 的 ID 参数已改为 Long)
        String token = utils.createJwt(userDetails, user.getUsername(), user.getId());

        // 6. 使用 Builder 构建 UserVO 并填充 roleName
        UserVO vo = UserVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())         // 返回 1 或 2
                .roleName(roleName)           // 返回 "学生" 或 "老师"
                .token(token)
                .build();

        return RestBean.success(vo);
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录接口")
    public RestBean<Void> logout(@RequestHeader("Authorization") String authorization) {
        // 如果是 "Bearer xxx" 格式，resolveJwt 内部通常会处理，
        // 这里 invalidateJwt 主要是把当前 token 加入黑名单
        if (utils.invalidateJwt(authorization)) {
            return RestBean.success();
        } else {
            return RestBean.failure(400, "退出登录失败，无效的令牌");
        }
    }
}