package com.example.demo.controller;

import org.modelmapper.ModelMapper;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.example.demo.dto.UserDTO;
import com.example.demo.entities.User;
import com.example.demo.service.contract.IAuthService;
import com.example.demo.service.imp.AuthService;

@RestController
@RequestMapping("/login")
public class AuthController {

    ModelMapper modelMapper;
    IAuthService authService;

    public AuthController(ModelMapper modelMapper, AuthService authService) {
        this.modelMapper = modelMapper;
        this.authService = authService;
    }

    @PostMapping()
    public ResponseEntity<UserDTO> loginByEmail(@RequestBody User user) throws BadRequest {
        return ResponseEntity
                .ok((modelMapper.map(authService.loginByEmail(user.getEmail(), user.getPassword()), UserDTO.class)));
    }
}