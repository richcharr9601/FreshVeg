package com.example.demo.entities;



import java.util.List;
import java.util.Set;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "address")
@SQLDelete(sql = "UPDATE address SET deleted = 1 WHERE address_id=?")
@Where(clause = "deleted=false")
public class Address {
   
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addressId;

	private String receiverName;
    private String receiverPhone;
	private String address;
	private Boolean deleted = Boolean.FALSE;


	@OneToMany(mappedBy = "address")
	@JsonIgnoreProperties("address")
	@JsonIgnore
	private Set<Order> orders;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "userId")
	@JsonBackReference
	private User user;
}
