package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.example.demo.dto.ChangePasswordDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.service.contract.IUserService;
import com.example.demo.service.imp.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    ModelMapper modelMapper;
    IUserService userService;

    public UserController(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;

    }

    

    @PostMapping()
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO) throws BadRequest {
        User user = modelMapper.map(userDTO, User.class);
        return ResponseEntity.ok(modelMapper.map(userService.add(user), UserDTO.class));
    }

    @PostMapping("/{userId}/role/{roleId}")
    public ResponseEntity<String> addUserRole(@PathVariable("userId") long userId, @RequestBody long roleId)
            throws BadRequest {
        Optional<Role> roleOptional = userService.addRole(userId, roleId);
        return roleOptional.map(r -> ResponseEntity.ok("role changed: " + r.getName()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> editUser(@PathVariable("userId") Long userId, @RequestBody UserDTO userDto)
            throws BadRequest {
                User user = modelMapper.map(userDto, User.class);
                user.setUserId(userId);
                UserDTO updatedUser = modelMapper.map(userService.editUser(userId, user), UserDTO.class);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Object> changePassword(@PathVariable("id") long id, @RequestBody ChangePasswordDTO changePasswordDTO)
            throws BadRequest {
                
        return  ResponseEntity.ok(userService.changePassword(id, changePasswordDTO));
               
    }
}
