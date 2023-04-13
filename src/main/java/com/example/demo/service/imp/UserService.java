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
        boolean existsById = userRepository.existsById(userId);

        if (existsById) {
            User user = userRepository.findById(userId).get();
            boolean isPasswordMatch = passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword());
            if(isPasswordMatch == true){
                user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
            userRepository.save(user);
            return "Change password successfully";
               
            }else return "Current password is not correct";
        }
        return "User is not exist";
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
        String formattedDateStr = userDTO.getBirthday();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try {
            date = formatter.parse(formattedDateStr);
            long unixTime = date.getTime() / 1000L;
            user.setBirthday(unixTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setAvatar(userDTO.getAvatar());
        user.setName(userDTO.getName());
        return userRepository.save(user);
    }
}
