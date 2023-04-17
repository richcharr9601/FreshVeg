package com.example.demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.token.AccessToken;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repository.entity.UserRepository;
import com.example.demo.security.payload.CustomOAuth2User;
import com.example.demo.security.service.JwtService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    private final JwtService jwtService;



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
    //    String email = oauthUser.getEmail();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        User user = userRepository.findByEmail(email);
        if(user == null) {
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setIsVerified(true);
            Role userRole = new Role(Long.valueOf(2));
            newUser.setRoles(Set.of(userRole));
            user = userRepository.save(newUser);
        }           


//         // Build the response body
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("email", email);
        responseBody.put("name", name);
        User user1 = userRepository.findByEmail(email);

        if(user1 != null && user.getIsVerified()==true){
            String jwt = jwtService.generateToken(Map.of("authorities", user1.getAuthorities()), user);
        responseBody.put("accessToken", jwt);
    }
        request.authenticate(response);
        // Write the response entity to the response
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        // response.sendRedirect("http://localhost:5173");
        response.sendRedirect("/auth/login-google");
        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
    }    
}