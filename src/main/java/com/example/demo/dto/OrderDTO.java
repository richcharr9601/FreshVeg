package com.example.demo.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.example.demo.entities.Address;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private String phone;
    private Double amount;
    private Boolean statusPayment;
    private String note;
    private Date orderDate;
    private Long addressId;

    @JsonManagedReference
    private Set<OrderDetailDTO> orderDetails;
}
