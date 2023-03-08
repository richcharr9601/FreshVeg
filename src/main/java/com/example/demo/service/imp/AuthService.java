package com.example.demo.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import com.example.demo.entities.User;
import com.example.demo.dto.UserDTO;

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

    public UserDTO loginByEmail(String email, String password) {
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
