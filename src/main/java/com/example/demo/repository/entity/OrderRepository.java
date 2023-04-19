package com.example.demo.repository.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.OrderDTO;
import com.example.demo.entities.Order;
import com.example.demo.entities.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query(value = "select * from orders where user_id = ?1", nativeQuery = true)
	List<Order> findOrderByUserId(Long id);

	Order findByOrderId(Long orderId);
	
	List<Order> findByUserUserId(Long userId);

}
