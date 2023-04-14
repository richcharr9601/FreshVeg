package com.example.demo.entities;

import java.io.Serializable;

import java.util.Set;

import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
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
@Table(name = "orders")
@SQLDelete(sql = "UPDATE orders SET deleted = 1 WHERE order_id=?")
@Where(clause = "deleted=false")
public class Order implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;
	// @Temporal(TemporalType.DATE)
	// @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Long orderDate;
	private int amount;
	@Nationalized
	private String phone;
	private String note;
	private Boolean statusPayment;
	@Enumerated(EnumType.STRING)
    private OrderStatus status;
	private Boolean deleted = Boolean.FALSE;

	public enum OrderStatus {
		Confirmed,
		onWaitingConfirm,
		Success,
		Failed
	}

	@PrePersist
    public void prePersist() {
        this.status = OrderStatus.onWaitingConfirm;
    }

	@OneToMany(mappedBy = "order")
	@JsonIgnore
	private Set<OrderDetail> orderDetails;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "addressId")
	@JsonIgnoreProperties(value = {"deleted"}, allowGetters = true)
	private Address address;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "userId")
	@JsonIgnore
	private User user;

}
