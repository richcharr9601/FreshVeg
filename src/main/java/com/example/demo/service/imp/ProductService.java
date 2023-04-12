package com.example.demo.service.imp;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entities.Category;
import com.example.demo.entities.Product;
import com.example.demo.repository.entity.ProductRepository;
import com.example.demo.service.contract.IProductService;

@Service
public class ProductService extends EntityService<Product, Long> implements IProductService {

    IProductService productService;

    @Autowired
    private ProductRepository productRepository;

    public ProductService() {
        super(Product.class);
    }

    public List<Product> findProductsWithSorting(String field) {
        return productRepository.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    public Page<Product> findProductsWithPagination(int offset, int pageSize) {
        Page<Product> products = productRepository.findAll(PageRequest.of(offset, pageSize));
        return products;
    }

    public Page<Product> findProductsWithPaginationAndSorting(int offset,int pageSize,String field){
        Page<Product> products = productRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field)));
        return  products;
    }

    public Product editProduct(Long id, ProductDTO productDTO) {
        long unixTime = System.currentTimeMillis() / 1000L;
        Product product = productRepository.findByProductId(id);
        product.setProductName(productDTO.getProductName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setWeight(productDTO.getWeight());
        product.setDiscount(productDTO.getDiscount());
        product.setEnteredDate(unixTime);
        product.setStatus(productDTO.getStatus());
        Category category = new Category();
        category.setCategoryId(productDTO.getCategoryId());
        product.setCategory(category);
        return productRepository.save(product);
    }
}
