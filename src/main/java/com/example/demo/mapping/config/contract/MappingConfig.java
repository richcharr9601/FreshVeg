package com.example.demo.mapping.config.contract;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public abstract class MappingConfig {
    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    protected ApplicationContext context;

    @PostConstruct
    public abstract void configure();
}
