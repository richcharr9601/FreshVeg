package com.example.demo.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private Long userId;
    private String address;
    private String phone;
    private Double amount;
    private int status;

    @JsonManagedReference
    private List<OrderDetailDTO> orderDetails;
}
