package com.example.demo.service.contract;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;

@Service
public interface IUserService extends IService<User, Long> {
    boolean changePassword(Long userId, String password);

    Optional<Role> addRole(Long userId, Long roleId);
    
}
