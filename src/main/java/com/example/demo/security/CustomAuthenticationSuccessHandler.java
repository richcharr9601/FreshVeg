package com.example.demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.entities.User;
import com.example.demo.repository.entity.UserRepository;
import com.example.demo.security.payload.CustomOAuth2User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
//        String email = oauthUser.getEmail();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

    
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        userRepository.save(user);

        // Build the response body
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("email", email);
        responseBody.put("name", name);

        // Write the response entity to the response
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("/auth/login-with-google");

        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
    }
}