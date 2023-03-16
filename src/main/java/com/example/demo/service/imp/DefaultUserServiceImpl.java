package com.example.demo.service.imp;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserRegisteredDTO;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repository.entity.RoleRepository;
import com.example.demo.repository.entity.UserRepository;
import com.example.demo.service.contract.DefaultUserService;

@Service
public class DefaultUserServiceImpl implements DefaultUserService {
    @Autowired
	private UserRepository userRepo;
	
   @Autowired
  	private RoleRepository roleRepo;
  	
   @Autowired
	 JavaMailSender javaMailSender;
   
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	
		User user = userRepo.findByEmail(email);
		if(user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		// return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getName()));
		return null;		
	}
	
	// private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
	// 	return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	// }

	@Override
	public User save(UserRegisteredDTO userRegisteredDTO) {
		// Role role = roleRepo.findByRole("USER");
		
		User user = new User();
		user.setEmail(userRegisteredDTO.getEmail());
		user.setName(userRegisteredDTO.getUsername());
		user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
		user.setRoles(null);
		
		return userRepo.save(user);
	}

	@Override
	public String generateOtp(User user) {
		try {
			int randomPIN = (int) (Math.random() * 9000) + 1000;
			user.setOtp(randomPIN);
			userRepo.save(user);
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setFrom("");
			msg.setTo(user.getEmail());

			msg.setSubject("Welcome To My Channel");
			msg.setText("Hello \n\n" +"Your Login OTP :" + randomPIN + ".Please Verify. \n\n"+"Regards \n"+"ABC");

			javaMailSender.send(msg);
			
			return "success";
			}catch (Exception e) {
				e.printStackTrace();
				return "error";
			}
	}

}
