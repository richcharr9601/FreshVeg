package com.example.demo.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	private Double weight;
	private double price;
	private int discount;
	@Nationalized
	private String description;
	@Temporal(TemporalType.DATE)
	private Date enteredDate;
	private Boolean status;
	private Boolean deleted = Boolean.FALSE;


	@OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
	@JsonManagedReference
    private Set<ProductImage> productImages;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "categoryId")
	private Category category;

}
