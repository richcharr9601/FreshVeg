package com.example.demo.dto;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.example.demo.entities.Category;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ProductDTO {
    private Long productId;
	private String productName;
	private int quantity;
	private double price;
	private int discount;
	private String description;
	private Date enteredDate;
	private Boolean status;


	// @JsonManagedReference
	private Set<ProductImageDTO> productImage;

	private Long categoryId;
}
