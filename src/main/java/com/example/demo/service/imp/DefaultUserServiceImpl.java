package com.example.demo.service.imp;

import java.util.Collection;
import java.util.Date;
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

import com.example.demo.dto.OTPCodeDTO;
import com.example.demo.dto.ResetPasswordDTO;
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
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
		// return null;		
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	public String register(UserRegisteredDTO userRegisteredDTO) {
		boolean existsByEmail = userRepo.existsByEmail(userRegisteredDTO.getEmail());

		if(existsByEmail==false){
		Role role = roleRepo.findByName("USER");
		User user = new User();
		long unixTime = System.currentTimeMillis() / 1000L;
		user.setEmail(userRegisteredDTO.getEmail());
		user.setName(userRegisteredDTO.getUsername());
		user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
		user.setRoles(Set.of(role));
		user.setIsVerified(false);
		user.setRegisterDate(unixTime);
		generateOtp(user);
		// userRepo.save(user);
		 userRepo.save(user);
		 return "Can register";}
		return "Email is already existed";
	}

	public String checkOTP(OTPCodeDTO otpCodeDTO){
		try{
		User user = userRepo.findByEmail(otpCodeDTO.getEmail());
		Boolean existByEmail = userRepo.existsByEmail(otpCodeDTO.getEmail());
		if(existByEmail &&(user.getOtp() == otpCodeDTO.getOtpCode())){
			user.setIsVerified(true);
			userRepo.save(user);
			return "Register Successfully";
		}
		return "Cannot Register, Please check input again";
	}catch (Exception e) {
		e.printStackTrace();
		return "error";
	}
	}

	public String forgotPassword(ResetPasswordDTO resetPasswordDTO){
		User user = userRepo.findByEmail(resetPasswordDTO.getEmail());
		Boolean existByEmail = userRepo.existsByEmail(resetPasswordDTO.getEmail());
		if(existByEmail){
			generateOtpForgorPassword(user);
			userRepo.save(user);
			return "Email is valid";
		}
		return "Email is not exist";
	}

	public String checkResetPasswordOTP(ResetPasswordDTO resetPasswordDTO){
		try{
		User user = userRepo.findByEmail(resetPasswordDTO.getEmail());
		Boolean existByEmail = userRepo.existsByEmail(resetPasswordDTO.getEmail());
		if(existByEmail &&(user.getOtp() == resetPasswordDTO.getOtpCode())){

			return "OTP is correct";
		}
		return "OTP is not correct";
	}catch (Exception e) {
		e.printStackTrace();
		return "error";
	}
	}

	public String ResetPasswordOTP(ResetPasswordDTO resetPasswordDTO){
		try{
		User user = userRepo.findByEmail(resetPasswordDTO.getEmail());
		{
			user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
			return "Reset password successfully";
		}
	}catch (Exception e) {
		e.printStackTrace();
		return "error";
	}
}

	@Override
	public String generateOtp(User user) {
		try {
			int randomPIN = (int) (Math.random() * 9000) + 1000;
			user.setOtp(randomPIN);
			// userRepo.save(user);
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setFrom("duclade150172@fpt.edu.vn");
			msg.setTo(user.getEmail());

			msg.setSubject("Welcome To FreshVeg");
			msg.setText("Hello \n\n" +"Your Register OTP :" + randomPIN + ".Please Verify. \n\n"+"Regards \n"+"FreshVeg");

			javaMailSender.send(msg);
			
			return "success";
			}catch (Exception e) {
				e.printStackTrace();
				return "error";
			}
	}

	@Override
	public String generateOtpForgorPassword(User user) {
		try {
			int randomPIN = (int) (Math.random() * 9000) + 1000;
			user.setOtp(randomPIN);
			// userRepo.save(user);
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setFrom("duclade150172@fpt.edu.vn");
			msg.setTo(user.getEmail());

			msg.setSubject("Welcome To FreshVeg");
			msg.setText("Hello \n\n" +"Your Reset Password OTP :" + randomPIN + ".Please reset password. \n\n"+"Regards \n"+"FreshVeg");

			javaMailSender.send(msg);
			
			return "success";
			}catch (Exception e) {
				e.printStackTrace();
				return "error";
			}
	}

}
