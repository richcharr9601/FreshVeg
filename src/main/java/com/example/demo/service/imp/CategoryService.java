package com.example.demo.service.imp;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Category;
import com.example.demo.service.contract.ICategoryService;

@Service
public class CategoryService extends EntityService<Category, Long> implements ICategoryService {

    public CategoryService() {
        super(Category.class);
    }

}
