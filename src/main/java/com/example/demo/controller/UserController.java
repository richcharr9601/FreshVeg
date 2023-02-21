package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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

import com.example.demo.dto.UserDTO;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.service.contract.IUserService;
import com.example.demo.service.imp.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    ModelMapper modelMapper;
    IUserService userService;

    public UserController(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(
                modelMapper.map(userService.findAll(), new TypeToken<List<UserDTO>>() {
                }.getType()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") long id) {
        Optional<User> userOptional = userService.findByID(id);
        return userOptional.map(c -> ResponseEntity.ok(modelMapper.map(c, UserDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<UserDTO> AddUser(@RequestBody UserDTO userDTO) throws BadRequest {
        User user = modelMapper.map(userDTO, User.class);
        return ResponseEntity.ok(modelMapper.map(userService.create(user), UserDTO.class));
    }

    @PostMapping("/{userId}/role/{roleId}")
    public ResponseEntity<String> addUserRole(@PathVariable("userId") long userId, @RequestBody long roleId)
            throws BadRequest {
        Optional<Role> roleOptional = userService.addRole(userId, roleId);
        return roleOptional.map(r -> ResponseEntity.ok("role changed: " + r.getName()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> editUser(@PathVariable("id") long id, @RequestBody UserDTO userDto)
            throws BadRequest {
        User user = modelMapper.map(userDto, User.class);
        Optional<User> updateOptional = userService.update(id, user);

        return updateOptional.map(c -> ResponseEntity.ok(modelMapper.map(c, UserDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Object> changePassword(@PathVariable("id") long id, @RequestBody String password)
            throws BadRequest {
        return userService.changePassword(id, password) ? ResponseEntity.ok("password changed: " + password)
                : ResponseEntity.notFound().build();
    }
}
