package com.example.demo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.example.demo.security.payload.LoginRequest;
import com.example.demo.security.payload.LoginResponse;
import com.example.demo.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.http.MediaType;


import com.example.demo.dto.ResetPasswordDTO;
import com.example.demo.dto.UserRegisteredDTO;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repository.entity.UserRepository;
import com.example.demo.service.contract.DefaultUserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    final ModelMapper modelMapper;
    
    final DefaultUserService defaultUserService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final Map<String, String> otpStore = new HashMap<>();

    private final UserRepository userRepository;

    private OAuth2AuthorizedClientService clientService;

    @PostMapping("login")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws BadRequest {
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        if(user.getIsVerified() == true){
        String jwt = jwtService.generateToken(Map.of(
            "authorities", user.getAuthorities()
        ), user);

        return ResponseEntity
                .ok(new LoginResponse(jwt, modelMapper.map(user, User.class)));}
                else return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        return ResponseEntity.ok("Logout successful");
    }

        
        @PostMapping("rspassword")
        public ResponseEntity<String> forgotPassword(@RequestBody ResetPasswordDTO resetPasswordDTO ){
                return ResponseEntity.ok(defaultUserService.forgotPassword(resetPasswordDTO));
            }
        @PostMapping("check-rspassword-otp")
        public ResponseEntity<String> checkResetPasswordOTP(@RequestBody ResetPasswordDTO resetPasswordDTO ){
            return ResponseEntity.ok(defaultUserService.checkResetPasswordOTP(resetPasswordDTO));
        }
        
        @PostMapping("rspassword-otp")
        public ResponseEntity<String> ResetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO ){
            return ResponseEntity.ok(defaultUserService.ResetPasswordOTP(resetPasswordDTO));
        }

        @PostMapping("/generate-otp")
        public ResponseEntity<String> registerUserAccount(@RequestBody UserRegisteredDTO userRegisteredDTO) {
        String email = userRegisteredDTO.getEmail();
        boolean existsByEmail = userRepository.existsByEmail(userRegisteredDTO.getEmail());

		if(existsByEmail==false){
        String otp = defaultUserService.generateOtp(userRegisteredDTO);
        otpStore.put(email, otp);
        return ResponseEntity.ok("Generate OTP successfully");}
        else return ResponseEntity.ok("Email is already existed");
    }

        @PostMapping("verify-otp")
        public ResponseEntity<String> verifyOtp(@RequestBody UserRegisteredDTO userRegisteredDTO) throws BadRequest{
        String email = userRegisteredDTO.getEmail();
        String otp = userRegisteredDTO.getOtp();
        if (otpStore.containsKey(email) && otpStore.get(email).equals(otp)) {
            // Xác thực thành công, tạo người dùng mới
            defaultUserService.register(userRegisteredDTO);
            otpStore.remove(email);
            return ResponseEntity.ok("OTP is correct");
        } else {
            // Xác thực thất bại
            return ResponseEntity.ok("OTP is not correct");
        }   
    }

    @GetMapping("login-google")
    public Map<String, Object> getUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return oAuth2User.getAttributes();
    }
    
    

        }
    
    
