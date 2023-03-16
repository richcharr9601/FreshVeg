package com.example.demo.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Nationalized;

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
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;
	@Temporal(TemporalType.DATE)
	private Date orderDate;
	private Double amount;
	@Nationalized
	private String phone;
	private int status;

	@OneToMany(mappedBy = "order")
	private List<OrderDetail> orderDetails;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "addressId")
	private Address address;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "userId")
	private User user;

}
