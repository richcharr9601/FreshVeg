package com.example.demo.entities;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailKey implements Serializable {
    @Column(name = "productId")
    @JsonIgnore
    Long productId;

    @Column(name = "orderId")
    @JsonIgnore
    Long orderId;
}
