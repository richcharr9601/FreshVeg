package com.example.demo.mapping.config;

import org.springframework.stereotype.Component;

import com.example.demo.mapping.config.contract.MappingConfig;

@Component
public class AddressMapping extends MappingConfig {

    @Override
    public void configure() {
        // modelMapper.createTypeMap(Address.class, AddressDTO.class)
        // .addMappings(mapper -> {
        // mapper.<Integer>map(s -> s.getClient().getId(), (d,v) -> d.setClientId(v));
        // });
    }

}
