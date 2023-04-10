package com.example.demo.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.example.demo.entities.Address;
import com.example.demo.entities.User;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Set<Address> findByUser(Optional<User> user);
    Address findByAddressId(Long addressId);
}
