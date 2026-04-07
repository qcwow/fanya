package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.po.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends IService<User>, UserDetailsService {
    User findUserByUsername(String username);
}
