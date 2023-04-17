package com.example.demo.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@SQLDelete(sql = "UPDATE products SET deleted = 1 WHERE product_id=?")
@Where(clause = "deleted=false")
public class Product implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;
	@Nationalized
	private String productName;
	private int weight;
	private double price;
	private int discount;
	@Nationalized
	private String description;
	// @Temporal(TemporalType.DATE)
	private Long enteredDate;
	private Boolean status;
	private Boolean deleted = Boolean.FALSE;


	@OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
	@JsonIgnore
    private Set<ProductImage> productImage;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "categoryId")
	@JsonIgnore
	private Category category;

}
