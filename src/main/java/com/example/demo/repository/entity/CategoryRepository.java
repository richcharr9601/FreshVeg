package com.example.demo.repository.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Category;
import com.example.demo.entities.Product;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByCategoryId(Long categoryId);
}
