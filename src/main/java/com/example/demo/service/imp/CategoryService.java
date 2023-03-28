package com.example.demo.service.imp;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Category;
import com.example.demo.service.contract.ICategoryService;
import com.example.demo.service.contract.IProductService;

import com.example.demo.entities.Product;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import com.example.demo.repository.entity.ProductRepository;
import com.example.demo.repository.entity.CategoryRepository;
import com.example.demo.repository.RepositoryWrapper;

@Service
public class CategoryService extends EntityService<Category, Long> implements ICategoryService {

    public CategoryService() {
        super(Category.class);
    }

    ModelMapper modelMapper;
    IProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> findAllProduct(Long categoryId) {
        List<Product> list = new ArrayList<>();
        list = productRepository.findAll();
        return list;
    }


    public Boolean deleteCategory(Long categoryId) {
        Boolean result = categoryRepository.existsById(categoryId);
        categoryRepository.deleteById(categoryId);
        return result;
    }
  
}
