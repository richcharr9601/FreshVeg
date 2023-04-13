package com.example.demo.service.contract;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OTPCodeDTO;
import com.example.demo.dto.ResetPasswordDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserRegisteredDTO;
import com.example.demo.entities.User;

@Service
public interface DefaultUserService extends UserDetailsService{

	String register(UserRegisteredDTO userRegisteredDTO);

	String generateOtp(UserRegisteredDTO userRegisteredDTO);
	
	String generateOtpForgorPassword(User user);

	String forgotPassword(ResetPasswordDTO resetPasswordDTO);
	
	String checkResetPasswordOTP(ResetPasswordDTO resetPasswordDTO);

	String ResetPasswordOTP(ResetPasswordDTO resetPasswordDTO);

}
