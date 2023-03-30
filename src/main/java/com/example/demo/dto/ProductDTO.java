package com.example.demo.dto;
import java.util.Date;

import com.example.demo.entities.Category;

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
	private String productImage;
	private String description;
	private Date enteredDate;
	private Boolean status;

	private Long categoryId;
}
