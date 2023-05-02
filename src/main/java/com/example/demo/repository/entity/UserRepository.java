package com.example.demo.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.StatisticUserDTO;
import com.example.demo.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
	boolean existsByEmail(String email);
    User findByUserId(Long userId);

	@Query(value = "SELECT u.USER_ID, u.NAME,u.AVATAR, COUNT(*) AS OrderCount FROM USERS u JOIN ORDERS o ON u.USER_ID = o.USER_ID GROUP BY u.NAME,u.AVATAR,u.USER_ID ORDER BY OrderCount DESC FETCH FIRST 10 ROWS ONLY", nativeQuery = true)
	List<StatisticUserDTO> list10UserWithMostOrder();

	@Query(value = "SELECT u.USER_ID, u.NAME,u.AVATAR, COUNT(*) AS OrderCount FROM USERS u JOIN ORDERS o ON u.USER_ID = o.USER_ID WHERE o.USER_ID = ? GROUP BY u.NAME,u.AVATAR,u.USER_ID ORDER BY OrderCount", nativeQuery = true)
	StatisticUserDTO userWithOrder (Long userId);

}
