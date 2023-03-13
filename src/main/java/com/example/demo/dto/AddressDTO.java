package com.example.demo.dto;


import java.util.List;

import com.example.demo.entities.Order;
import com.example.demo.entities.User;

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
public class AddressDTO {
    private Long addressId;
	private String receiverName;
    private String receiverPhone;
	private String address;
	
	private Long userId;
}
