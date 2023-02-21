package com.example.demo.entities;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Nationalized;

import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
	private Long id;
	@Nationalized
	private String name;
	private double unitPrice;
	private int quantity;
	private double totalPrice;
	private Product product;
}
