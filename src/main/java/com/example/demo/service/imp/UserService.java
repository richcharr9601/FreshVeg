package com.example.demo.service.imp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ChangePasswordDTO;
import com.example.demo.dto.UserDTO;
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

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public String changePassword(Long userId, ChangePasswordDTO changePasswordDTO) {
            User user = userRepository.findById(userId).get();
            boolean isPasswordMatch = passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword());
            if(isPasswordMatch == true){
                user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
            userRepository.save(user);
            return "Change password successfully";
               
            }else return "Current password is not correct";
    }

    @Override
    public Optional<Role> addRole(Long userId, Long roleId) {
        boolean userExist = userRepository.existsById(userId);
        boolean roleExist = roleRepository.existsById(roleId);

        if (userExist && roleExist) {
            User user = userRepository.findById(userId).get();
            Role role = roleRepository.findById(userId).get();
            user.setRoles(Set.of(role));
            userRepository.save(user);

            return Optional.of(role);
        }
        return Optional.empty();
    }

    public User editUser (Long userId, UserDTO userDTO){
        User user = userRepository.findByUserId(userId);
        user.setAvatar(userDTO.getAvatar());
        user.setName(userDTO.getName());
        user.setBirthday(userDTO.getBirthday());
        user.setAddress(user.getAddress());
        user.setEmail(user.getEmail());
        user.setIsVerified(user.getIsVerified());
        user.setOtp(user.getOtp());
        user.setPassword(user.getPassword());
        user.setRegisterDate(user.getRegisterDate());
        user.setRoles(user.getRoles());
        user.setUserId(userId);
        user.setStatus(user.getStatus());
        return userRepository.save(user);
    }
}
