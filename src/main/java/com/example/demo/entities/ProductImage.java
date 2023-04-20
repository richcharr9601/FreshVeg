package com.example.demo.entities;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productImage")
public class ProductImage implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productImageId;
    private String imageLink;

    @ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "productId")
	private Product product;
}
