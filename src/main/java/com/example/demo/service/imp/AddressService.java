package com.example.demo.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.modelmapper.ModelMapper;

import java.util.Set;
import java.util.Optional;
import java.util.ArrayList;

import com.example.demo.dto.AddressDTO;
import com.example.demo.entities.Address;
import com.example.demo.entities.User;
import com.example.demo.repository.RepositoryWrapper;
import com.example.demo.repository.entity.OrderRepository;
import com.example.demo.repository.entity.AddressRepository;
import com.example.demo.repository.entity.ProductRepository;
import com.example.demo.repository.entity.UserRepository;
import com.example.demo.service.contract.IAddressService;
import com.example.demo.service.contract.IUserService;

@Service
public class AddressService extends EntityService<Address, Long> implements IAddressService {

    IAddressService productService;
    ModelMapper modelMapper;
    IUserService userService;

    @Autowired
    private ProductRepository productRepository;

    public AddressService() {
        super(Address.class);
    }

    @Autowired
    private RepositoryWrapper repositoryWrapper;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;



}