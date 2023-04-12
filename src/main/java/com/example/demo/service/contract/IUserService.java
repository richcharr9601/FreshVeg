package com.example.demo.service.contract;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ChangePasswordDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;

@Service
public interface IUserService extends IService<User, Long> {
    String changePassword(Long userId, ChangePasswordDTO changePasswordDTO);

    Optional<Role> addRole(Long userId, Long roleId);

    User editUser (Long userId, UserDTO userDTO);
    
}
