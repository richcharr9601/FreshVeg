package com.example.demo.entities;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orderDetails")
public class OrderDetail implements Serializable {

	@Id
	@EmbeddedId
	private OrderDetailKey id = new OrderDetailKey();
	private int quantity;
	private Double price;

	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("productId")
	@JoinColumn(name = "productId")
	private Product product;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@MapsId("orderId")
	@JoinColumn(name = "orderId")
	@JsonBackReference
	private Order order;
}
