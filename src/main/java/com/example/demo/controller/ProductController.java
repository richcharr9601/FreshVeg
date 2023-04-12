package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductImageDTO;
import com.example.demo.entities.Category;
import com.example.demo.entities.Product;
import com.example.demo.entities.ProductImage;
import com.example.demo.repository.entity.ProductImageRepository;
import com.example.demo.repository.entity.ProductRepository;
import com.example.demo.service.contract.ICategoryService;
import com.example.demo.service.contract.IOrderDetailService;
import com.example.demo.service.contract.IOrderService;
import com.example.demo.service.contract.IProductImageService;
import com.example.demo.service.contract.IProductService;
import com.example.demo.service.imp.CategoryService;
import com.example.demo.service.imp.OrderDetailService;
import com.example.demo.service.imp.ProductImageService;
import com.example.demo.service.imp.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

    ModelMapper modelMapper;
    IOrderService orderService;
    IOrderDetailService orderDetailService;
    ICategoryService categoryService;
    IProductService productService;
    IProductImageService productImageService; 

    public ProductController(ModelMapper modelMapper, CategoryService categoryService,ProductService productService,ProductImageService productImageService, OrderDetailService orderDetailService) {
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
        this.orderDetailService = orderDetailService;
        this.productService = productService;
        this.productImageService = productImageService;

    }
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    
    
    @PostMapping()
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDto)
            throws BadRequest {
                Date date = new Date();
                Product product = modelMapper.map(productDto, Product.class);
                product.setEnteredDate(date);
                productService.add(product);
                product.getProductImage().forEach(od -> {
                    productImageService.add(od);
                });

                 return ResponseEntity.ok(modelMapper.map(product, ProductDTO.class));

    }

    @GetMapping("/all/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategoryId(@PathVariable("categoryId") long id) {
        return ResponseEntity.ok(
                modelMapper.map(categoryService.findAllProduct(id), new TypeToken<List<Product>>() {
                }.getType()));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductsByProductId(@PathVariable("productId") long id) {
        Optional<Product> findOptional = productService.findByID(id);
        return findOptional.map(c -> ResponseEntity.ok(modelMapper.map(c, ProductDTO.class)))
        .orElse(ResponseEntity.notFound().build()); 
    }

    @GetMapping("/get")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@RequestBody List<Category> categoryIds) {
        return ResponseEntity.ok(modelMapper.map(productRepository.findByCategoryIn(categoryIds), new TypeToken<List<ProductDTO>>() {
        }.getType()));
    }

    
    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getProducts() {
        return ResponseEntity.ok(
                modelMapper.map(productService.findAll(), new TypeToken<List<ProductDTO>>() {
                }.getType()));
    }

    @PutMapping("{productId}")
    public ResponseEntity<ProductDTO> editProduct(@PathVariable("productId") Long id,
            @RequestBody ProductDTO productDTO)
            throws BadRequest {
            return ResponseEntity.ok(modelMapper.map(productService.editProduct(id,productDTO), ProductDTO.class));
            }

    @DeleteMapping()
    public ResponseEntity<String> deleteProduct(@RequestBody Product product)
    throws BadRequest {
    Boolean result = productRepository.existsById(product.getProductId());
    if (result) {
    productRepository.deleteById(product.getProductId());
    return ResponseEntity.ok("Product with ID" + product.getProductId() + " has been deleted");
    }
    return ResponseEntity.ok("Cannot delete");

    
}
}
