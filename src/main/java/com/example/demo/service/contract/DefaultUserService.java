package com.example.demo.service.contract;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserRegisteredDTO;
import com.example.demo.entities.User;

@Service
public interface DefaultUserService extends UserDetailsService{

	User save(UserRegisteredDTO userRegisteredDTO);

	String generateOtp(User user);
}
