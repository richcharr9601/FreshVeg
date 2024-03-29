package com.example.demo.repository.entity;

import java.util.List;

import org.hibernate.annotations.Filter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Order;
import com.example.demo.entities.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query(value = "select * from orders where user_id = ?1", nativeQuery = true)
	List<Order> findOrderByUserId(Long id);

	Order findByOrderId(Long orderId);
	

	@EntityGraph(attributePaths = {"user", "address", "orderDetails"}) // Optional: Use entity graph to fetch related entities
    @Filter(name = "deletedOrderFilter", condition = "deleted = false") // Enable the filter to exclude soft deleted records
	List<Order> findByUserUserId(Long userId);

	
}
