package com.example.demo.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserRegisteredDTO;
import com.example.demo.repository.entity.RoleRepository;
import com.example.demo.repository.entity.UserRepository;
import com.example.demo.service.contract.IAuthService;

@Service
public class AuthService extends EntityService<UserDTO, Long> implements IAuthService {
    public AuthService(ModelMapper modelMapper) {
        super(UserDTO.class);
        this.modelMapper = modelMapper;

    }

    ModelMapper modelMapper;
    IAuthService authService;

    @Autowired
    private UserRepository userRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public UserDTO loginByEmail(String email, String password) {
        boolean existsByID = userRepository.existsByEmail(email);     
            User user = userRepository.findByEmail(email);
        UserDTO nullUser = new UserDTO();
        if (user.getEmail().equals(email)) {
            if (user.getPassword().equals(password)) {
                UserDTO userNew = modelMapper.map(user, UserDTO.class);
                return userNew;
            }
        }
        
        return nullUser;
    
    }

    public Boolean logout(){
        return  true;
    }

    
}
