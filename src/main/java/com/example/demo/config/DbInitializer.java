package com.example.demo.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repository.RepositoryWrapper;
import com.example.demo.repository.entity.RoleRepository;
import com.example.demo.repository.entity.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@ConditionalOnProperty(name = "app.db-init", havingValue = "true")
public class DbInitializer implements CommandLineRunner {
    @Autowired
    RepositoryWrapper repositoryWrapper;

    @Override
    public void run(String... args) throws Exception {
        rolesInitialize();
        userInitialize();
    }

    private void rolesInitialize() throws JsonMappingException, JsonProcessingException, IOException {
        RoleRepository roleRepository = repositoryWrapper.getRoleRepository();
        roleRepository.deleteAll();

        File file = new ClassPathResource("data/role.json").getFile();
        String json = Files.readString(file.toPath());
        List<Role> roles = new ObjectMapper().readerForListOf(Role.class).readValue(json);

        roleRepository.saveAll(roles);
    }

    public void userInitialize() throws JsonMappingException, JsonProcessingException, IOException {
        UserRepository userRepository = repositoryWrapper.getUserRepository();
        userRepository.deleteAll();

        File file = new ClassPathResource("data/user.json").getFile();
        String json = Files.readString(file.toPath());
        List<User> users = new ObjectMapper().readerForListOf(User.class).readValue(json);
        
        userRepository.saveAll(users);
    }

}
