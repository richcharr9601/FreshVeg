package com.example.demo.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.example.demo.entities.Product;
import com.example.demo.entities.ProductImage;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.entities.Category;
import com.example.demo.entities.Order;
import com.example.demo.repository.RepositoryWrapper;
import com.example.demo.repository.entity.ProductRepository;
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
        Initialize(new Class[] { Role.class, User.class, Category.class, Product.class, ProductImage.class});
    }

    private void Initialize(Class[] entityTypes) {
        List.of(entityTypes).forEach(t -> {
            JpaRepository<Object, Object> repository = repositoryWrapper.repository(t);
            repository.deleteAll();

            List<Role> entities = new ArrayList<>();
            try {
                String path = String.format("data/%s.json", t.getSimpleName().toLowerCase());
                File file = new ClassPathResource(path).getFile();
                String json = Files.readString(file.toPath());
                entities = new ObjectMapper().readerForListOf(t).readValue(json);
            } catch (IOException e) {
                e.printStackTrace();
            }

            repository.saveAll(entities);
        });
    }
}
