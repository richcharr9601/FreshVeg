package com.example.demo.service.contract;

import org.springframework.stereotype.Service;

import com.example.demo.dto.UserDTO;

@Service
public interface IAuthService extends IService<UserDTO, Long> {
    UserDTO loginByEmail(String email, String password);
    Boolean logout();
}
