package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.example.demo.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private Long orderId;
    private Product product;
    private int weight;
    private Double price;

    @JsonBackReference
    private OrderDTO order;
}
