package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

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
import com.example.demo.entities.Product;
import com.example.demo.repository.entity.ProductRepository;
import com.example.demo.service.contract.ICategoryService;
import com.example.demo.service.contract.IOrderDetailService;
import com.example.demo.service.contract.IOrderService;
import com.example.demo.service.contract.IProductService;
import com.example.demo.service.imp.CategoryService;
import com.example.demo.service.imp.OrderDetailService;
import com.example.demo.service.imp.ProductService;

@RestController
@RequestMapping("/product")
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
    
    @Autowired
    private ProductRepository productRepository;

    
    
    @PostMapping()
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDto)
            throws BadRequest {
                Product product = modelMapper.map(productDto, Product.class);
        return ResponseEntity.ok(modelMapper.map(productService.add(product), ProductDTO.class));
    }

    @GetMapping("/all/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategoryId(@PathVariable("categoryId") long id) {
        return ResponseEntity.ok(
                modelMapper.map(categoryService.findAllProduct(id), new TypeToken<List<Product>>() {
                }.getType()));
    }

    
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(
                modelMapper.map(productService.findAll(), new TypeToken<List<Product>>() {
                }.getType()));
    }

    @PutMapping("{productId}")
    public ResponseEntity<ProductDTO> editProduct(@PathVariable("productId") Long id,
            @RequestBody ProductDTO productDTO)
            throws BadRequest {
            Product product = modelMapper.map(productDTO, Product.class);
            Optional<Product> updateOptional = productService.update(id, product);
        return updateOptional.map(c -> ResponseEntity.ok(modelMapper.map(c, ProductDTO.class)))
        .orElse(ResponseEntity.notFound().build());
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
