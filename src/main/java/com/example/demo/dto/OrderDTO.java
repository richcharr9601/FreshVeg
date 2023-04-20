package com.example.demo.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.example.demo.entities.Address;
import com.example.demo.entities.Order.OrderStatus;
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
    private int amount;
    private Boolean statusPayment;
    private OrderStatus status;
    private String note;
    private Date orderDate;
    private AddressDTO address;

    @JsonManagedReference
    private Set<OrderDetailDTO> orderDetails;
}
