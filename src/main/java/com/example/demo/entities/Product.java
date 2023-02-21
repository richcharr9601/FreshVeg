package com.example.demo.entities;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;
	@Nationalized
	private String productName;
	private int quantity;
	private double price;
	private int discount;
	private String productImage;
	@Nationalized
	private String description;
	@Temporal(TemporalType.DATE)
	private Date enteredDate;
	private Boolean status;
	public boolean favorite;

	@ManyToOne
	@JoinColumn(name = "categoryId")
	private Category category;

}
