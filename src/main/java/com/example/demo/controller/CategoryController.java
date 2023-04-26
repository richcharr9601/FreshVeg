package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import com.example.demo.dto.CategoryDTO;
import com.example.demo.entities.Category;
import com.example.demo.repository.entity.CategoryRepository;
import com.example.demo.service.contract.ICategoryService;
import com.example.demo.service.imp.CategoryService;
import com.example.demo.service.contract.IProductService;
import com.example.demo.service.imp.ProductService;

@RequestMapping("/category")
@RestController
public class CategoryController {

    ModelMapper modelMapper;
    ICategoryService categoryService;
    IProductService productService;
    private CategoryRepository categoryRepository;


    public CategoryController(ModelMapper modelMapper, CategoryService categoryService, ProductService productService) {
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
        this.productService = productService;

    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getCategory() {
        return ResponseEntity.ok(
                modelMapper.map(categoryService.findAll(), new TypeToken<List<CategoryDTO>>() {
                }.getType()));
    }
    
    @PostMapping()
    public ResponseEntity<CategoryDTO> AddCategory(@RequestBody CategoryDTO categoryDTO) throws BadRequest {
        Category category = modelMapper.map(categoryDTO, Category.class);
        return ResponseEntity.ok(modelMapper.map(categoryService.add(category), CategoryDTO.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable("id") long id) {
        Optional<Category> CategoryOptional = categoryService.findByID(id);
        return CategoryOptional.map(c -> ResponseEntity.ok(modelMapper.map(c, Category.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> editCategory(@PathVariable("id") long id, @RequestBody CategoryDTO categoryDTO)
            throws BadRequest {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Optional<Category> updateOptional = categoryService.update(id, category);
        return updateOptional.map(c -> ResponseEntity.ok(modelMapper.map(c, CategoryDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Boolean> deleteCategory(@PathVariable("categoryId") Long id)
            throws BadRequest {
        return categoryService.deleteCategory(id) ? ResponseEntity.ok(true) : ResponseEntity.badRequest().build();
    }

   

   }
