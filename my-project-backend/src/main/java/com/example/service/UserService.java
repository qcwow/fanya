package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.SyncUserDTO;
import com.example.entity.po.User;
import com.example.entity.vo.SyncUserVO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends IService<User>, UserDetailsService {
    User findUserByUsername(String username);

    SyncUserVO syncUser(SyncUserDTO dto);
}
