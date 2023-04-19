package com.example.demo.mapping.config;

import org.springframework.stereotype.Component;

import com.example.demo.dto.AddressDTO;
import com.example.demo.entities.Address;
import com.example.demo.mapping.config.contract.MappingConfig;

@Component
public class AddressMapping extends MappingConfig {

    @Override
    public void configure() {

        // modelMapper.typeMap(Address.class, AddressDTO.class).addMappings(mapper -> {
        //     mapper.map(src -> src.getUser().getUserId(), (dest, v) -> dest.setUserId((Long)v));
        // });

        // modelMapper.typeMap(AddressDTO.class, Address.class).addMappings(mapper -> {
        //     mapper.map(src -> src.getUserId(), (dest, v) -> dest.getUser().setUserId((Long)v));
        // });

    }

}
