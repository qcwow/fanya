package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.po.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User findUserByUsername(String username) {
        return this.query()
                .eq("username", username)
                .one(); // 查询一条记录
    }

    /**
     * Spring Security 认证核心方法
     * 将数据库的 User PO 转换为 Security 需要的 UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 获取数据库用户信息
        User user = this.findUserByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        // 2. 将 Integer 类型的 role 映射为字符串角色名
        // 逻辑一致：2 为 TEACHER，1 或其他为 STUDENT
        String roleName = (user.getRole() != null && user.getRole() == 2) ? "TEACHER" : "STUDENT";

        // 3. 构建并返回 UserDetails 对象
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                /*
                   .roles() 方法会自动在传入的字符串前加上 "ROLE_" 前缀
                   即最终权限为 ROLE_STUDENT 或 ROLE_TEACHER
                */
                .roles(roleName)
                .build();
    }
}