package com.example.demo.service.imp;

import org.springframework.stereotype.Service;

import com.example.demo.entities.ProductImage;
import com.example.demo.service.contract.IProductImageService;

@Service
public class ProductImageService extends EntityService<ProductImage, Long> implements IProductImageService { 
   
    public ProductImageService() {
        super(ProductImage.class);
    }

}
