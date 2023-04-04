package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.example.demo.dto.OTPCodeDTO;
import com.example.demo.dto.ResetPasswordDTO;
import com.example.demo.dto.UserRegisteredDTO;
import com.example.demo.entities.User;
import com.example.demo.repository.entity.UserRepository;
import com.example.demo.service.contract.DefaultUserService;
import com.nimbusds.jwt.JWTClaimsSet;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    final ModelMapper modelMapper;
    
    final DefaultUserService defaultUserService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final OAuth2AuthorizedClientService authorizedClientService;

    private final UserRepository userRepository;


    @PostMapping("login")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<LoginResponse> loginByEmail(@RequestBody LoginRequest loginRequest) throws BadRequest {
        
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

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@AuthenticationPrincipal OAuth2User oauth2User,
                                            OAuth2AuthenticationToken authentication) {

        // // Lấy thông tin người dùng đăng nhập thành công từ Google
        // OAuth2AuthorizedClient client = authorizedClientService
        //         .loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());

        // Lấy thông tin user từ OAuth2User
        String name = oauth2User.getAttribute("name");
        String email = oauth2User.getAttribute("email");
        
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        userRepository.save(user);
        ;
        
        return ResponseEntity.ok("Đăng nhập thành công!");
    }

    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> user(@AuthenticationPrincipal OAuth2User oauth2User) {
        // Trả về thông tin người dùng
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", oauth2User.getAttribute("name"));
        userInfo.put("email", oauth2User.getAttribute("email"));
        userInfo.put("picture", oauth2User.getAttribute("picture"));

        return ResponseEntity.ok(userInfo);
    }









    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        return ResponseEntity.ok("Logout successful");
    }

    @Autowired
    private DefaultUserService userService;

	    @PostMapping("register")
	    public ResponseEntity<User> registerUserAccount(@RequestBody
	              UserRegisteredDTO registrationDto) {
	        	    return ResponseEntity.ok(userService.register(registrationDto)); 
	    }

        @PostMapping("check-otp")
        public ResponseEntity<String> checkOTP(@RequestBody OTPCodeDTO otpCodeDTO ){
            return ResponseEntity.ok(defaultUserService.checkOTP(otpCodeDTO));
        }
        
        @PostMapping("rspassword")
        public ResponseEntity<User> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO ){
                return ResponseEntity.ok(defaultUserService.forgotPassword(resetPasswordDTO));
            }
        @PostMapping("check-rspassword-otp")
        public ResponseEntity<String> checkResetPasswordOTP(@RequestBody ResetPasswordDTO resetPasswordDTO ){
            return ResponseEntity.ok(defaultUserService.checkResetPasswordOTP(resetPasswordDTO));
        }
}