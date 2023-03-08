package com.example.demo.service.imp;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

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
}
