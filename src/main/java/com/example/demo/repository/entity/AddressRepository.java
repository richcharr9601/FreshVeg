package com.example.demo.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


import com.example.demo.entities.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query(value = "select * from address inner join users on address.USER_ID = users.userId where address.USER_ID = ?1", nativeQuery = true)
	List<Address>findAddressTest(@Param("id") Long id);
}
