package com.example.demo.service.contract;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Product;

@Service
public interface IProductService extends IService<Product, Long> {
    List<Product> findProductsWithSorting(String field);
    Page<Product> findProductsWithPagination(int offset, int pageSize);
    Page<Product> findProductsWithPaginationAndSorting(int offset,int pageSize,String field);
}
