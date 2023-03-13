package com.example.demo.service.contract;

import java.util.List;

import org.springframework.stereotype.Service;
import com.example.demo.entities.Category;
import com.example.demo.entities.Product;

@Service
public interface ICategoryService extends IService<Category, Long> {
    List<Product> findAllProduct(Long categoryId);
    Product editProduct(Long categoryId, Long id, Product product);
    Boolean deleteCategory(Long id);

}
