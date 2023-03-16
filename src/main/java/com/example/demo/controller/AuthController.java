package com.example.demo.controller;

import org.modelmapper.ModelMapper;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserRegisteredDTO;
import com.example.demo.entities.User;
import com.example.demo.service.contract.DefaultUserService;
import com.example.demo.service.contract.IAuthService;
import com.example.demo.service.imp.AuthService;
import com.example.demo.service.imp.EmailService;
import com.example.demo.service.imp.UserService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    ModelMapper modelMapper;
    IAuthService authService;

    public AuthController(ModelMapper modelMapper, AuthService authService) {
        this.modelMapper = modelMapper;
        this.authService = authService;
    }

    @PostMapping("login")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<UserDTO> loginByEmail(@RequestBody User user) throws BadRequest {
        return ResponseEntity
                .ok((modelMapper.map(authService.loginByEmail(user.getEmail(), user.getPassword()), UserDTO.class)));
    }

    @GetMapping("/login-google")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<String> loginByGoogle() {
    return ResponseEntity.ok("hello");
  }

    public String logout(){
        return "/home";
    }

    @Autowired
    private EmailService emailService;

   

    // @PostMapping("/register")
    // public ResponseEntity<User> registerUser(@RequestBody User user) throws MessagingException {
    //     User savedUser = userService.add(user);
    //     emailService.sendEmail(savedUser.getEmail(), savedUser.getName());
    //     return ResponseEntity.ok(savedUser);
    // }

    @Autowired
    private DefaultUserService userService;

	    @ModelAttribute("user")
	    public UserRegisteredDTO userRegistrationDto() {
	        return new UserRegisteredDTO();
	    }

	    @GetMapping
	    public String showRegistrationForm() {
	        return "register";
	    }

	    @PostMapping("register")
	    public String registerUserAccount(@RequestBody
	              UserRegisteredDTO registrationDto) {
	        userService.save(registrationDto);
	        return "redirect:/login";
	    }
}