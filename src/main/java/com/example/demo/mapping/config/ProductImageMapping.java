package com.example.demo.mapping.config;

import org.springframework.stereotype.Component;

import com.example.demo.dto.ProductImageDTO;
import com.example.demo.entities.ProductImage;
import com.example.demo.mapping.config.contract.MappingConfig;

@Component
public class ProductImageMapping extends MappingConfig {

    @Override
    public void configure() {

        modelMapper.typeMap(ProductImage.class, ProductImageDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getProduct().getProductId(), (dest, v) -> dest.getProduct().setProductId((Long)v));
        });

        modelMapper.typeMap(ProductImageDTO.class, ProductImage.class).addMappings(mapper -> {
            mapper.map(src -> src.getProduct().getProductId(), (dest, v) -> dest.getProduct().setProductId((Long)v));
        });

    }

}
