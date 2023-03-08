package com.example.demo.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entities.Product;
import com.example.demo.service.contract.ICategoryService;
import com.example.demo.service.contract.IOrderDetailService;
import com.example.demo.service.contract.IOrderService;
import com.example.demo.service.contract.IProductService;
import com.example.demo.service.imp.CategoryService;
import com.example.demo.service.imp.OrderDetailService;
import com.example.demo.service.imp.ProductService;

public class ProductController {

    ModelMapper modelMapper;
    IOrderService orderService;
    IOrderDetailService orderDetailService;
    ICategoryService categoryService;
    IProductService productService;


    public ProductController(ModelMapper modelMapper, CategoryService categoryService,ProductService productService, OrderDetailService orderDetailService) {
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
        this.orderDetailService = orderDetailService;
        this.productService = productService;

    }
    
    @PostMapping("/{categoryId}")
    public ResponseEntity<ProductDTO> addProduct(@PathVariable("categoryId") long id, @RequestBody ProductDTO productDto)
            throws BadRequest {
                Product product = modelMapper.map(productDto, Product.class);
        return ResponseEntity.ok(modelMapper.map(productService.add(product), ProductDTO.class));
    }

    @GetMapping("/all/{categoryId}")
    public ResponseEntity<List<Product>> getProduct(@PathVariable("categoryId") long id) {
        return ResponseEntity.ok(
                modelMapper.map(categoryService.findAllProduct(id), new TypeToken<List<Product>>() {
                }.getType()));
    }

    @PutMapping("/{categoryId}/{id}")
    public ResponseEntity<Product> editProduct(@PathVariable("categoryId") long cid, @PathVariable("id") long id,
            @RequestBody Product product)
            throws BadRequest {
        return ResponseEntity.ok(modelMapper.map(categoryService.editProduct(cid, id, product), Product.class));
    }

    // @DeleteMapping("/{categoryId}")
    // public Boolean deleteProduct(@PathVariable("categoryId") Long id,@RequestBody
    // Product product)
    // throws BadRequest {
    // categoryService.deleteProduct(product.getProductId(), product);
    // return true;
    // }
}
