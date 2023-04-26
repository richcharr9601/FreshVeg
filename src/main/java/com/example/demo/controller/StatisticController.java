package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.APIResponse;
import com.example.demo.dto.ProductDTO;
import com.example.demo.entities.Product;
import com.example.demo.entities.User;
import com.example.demo.repository.entity.ProductRepository;
import com.example.demo.repository.entity.UserRepository;
import com.example.demo.service.contract.ICategoryService;
import com.example.demo.service.contract.IProductService;
import com.example.demo.service.imp.CategoryService;
import com.example.demo.service.imp.ProductService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@RestController
@RequestMapping("/statistic")
public class StatisticController {

    ModelMapper modelMapper;
    ICategoryService categoryService;
    IProductService productService;


    public StatisticController(ModelMapper modelMapper, CategoryService categoryService,
            ProductService productService) {
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
        this.productService = productService;

    }

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{field}")
    public APIResponse<List<Product>> getProductWithSort(@PathVariable String field) {
        List<Product> allProducts = productService.findProductsWithSorting(field);
        return new APIResponse<>(allProducts.size(), allProducts);
    }

    @GetMapping("/pagination/{offset}/{pageSize}")
    public APIResponse<Page<Product>> getProductWithPagination(@PathVariable int offset, @PathVariable int pageSize) {
        Page<Product> productWithPagination = productService.findProductsWithPagination(offset, pageSize);
        return new APIResponse<>(productWithPagination.getSize(), productWithPagination);
    }

    @GetMapping("/pagination/{offset}/{pageSize}/{field}")
    public APIResponse<Page<Product>> getProductWithPaginationAndSort(@PathVariable int offset,
            @PathVariable int pageSize, @PathVariable String field) {
        Page<Product> findProductsWithPaginationAndSorting = productService.findProductsWithPaginationAndSorting(offset,
                pageSize, field);
        return new APIResponse<>(findProductsWithPaginationAndSorting.getSize(), findProductsWithPaginationAndSorting);
    }

    @GetMapping("/product-new")
    public ResponseEntity<List<ProductDTO>> getProductNew() {
        List<Product> newProducts = productRepository.listProductNew20();
        return ResponseEntity.ok(modelMapper.map(productRepository.listProductNew20(), new TypeToken<List<ProductDTO>>() {
        }.getType()));
    }
}
