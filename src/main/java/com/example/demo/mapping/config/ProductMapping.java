package com.example.demo.mapping.config;

import org.springframework.stereotype.Component;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entities.Product;
import com.example.demo.mapping.config.contract.MappingConfig;


@Component
public class ProductMapping extends MappingConfig {
    @Override
    public void configure() {
        modelMapper.typeMap(Product.class, ProductDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getCategory().getCategoryId(), (dest, v) -> dest.setCategoryId((Long)v));
        });

        modelMapper.typeMap(ProductDTO.class, Product.class).addMappings(mapper -> {
            mapper.map(src -> src.getCategoryId(), (dest, v) -> dest.getCategory().setCategoryId((Long)v));
        });
    }
}
