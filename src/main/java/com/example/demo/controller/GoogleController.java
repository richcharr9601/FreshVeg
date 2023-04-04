package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.User;
import com.example.demo.repository.entity.UserRepository;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping()
@RequiredArgsConstructor
public class GoogleController {

    
    private final OAuth2AuthorizedClientService authorizedClientService;

    private final UserRepository userRepository;
    
    @GetMapping("login/oauth2/code/google")
    public ResponseEntity<String> callback(@AuthenticationPrincipal OAuth2User oauth2User,
                                            OAuth2AuthenticationToken authentication) {

        // Lấy thông tin người dùng đăng nhập thành công từ Google
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());

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


}
