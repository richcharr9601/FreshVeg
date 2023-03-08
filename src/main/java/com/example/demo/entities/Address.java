package com.example.demo.entities;



import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Address {
    private Long addressId;
	private String receiverName;
    private String receiverPhone;
	private String address;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@MapsId("orderId")
	@JoinColumn(name = "orderId")
	@JsonBackReference
	private Order order;
}
