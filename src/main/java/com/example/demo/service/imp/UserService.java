package com.example.demo.service.imp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repository.entity.RoleRepository;
import com.example.demo.repository.entity.UserRepository;
import com.example.demo.service.contract.IUserService;

@Service
public class UserService extends EntityService<User, Long> implements IUserService {
    public UserService() {
        super(User.class);
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public boolean changePassword(Long userId, String passowrd) {
        boolean existsById = userRepository.existsById(userId);

        if (existsById) {
            User user = userRepository.findById(userId).get();
            user.setPassword(passowrd);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Role> addRole(Long userId, Long roleId) {
        boolean userExist = userRepository.existsById(userId);
        boolean roleExist = roleRepository.existsById(roleId);

        if (userExist && roleExist) {
            User user = userRepository.findById(userId).get();
            Role role = roleRepository.findById(userId).get();
            user.setRoles(List.of(role));
            userRepository.save(user);

            return Optional.of(role);
        }
        return Optional.empty();
    }
}
