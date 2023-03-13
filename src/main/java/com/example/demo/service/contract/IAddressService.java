package com.example.demo.service.contract;


import org.springframework.stereotype.Service;

import java.util.List;

import com.example.demo.entities.Address;
import com.example.demo.entities.User;

@Service
public interface IAddressService extends IService<Address, Long> {
}