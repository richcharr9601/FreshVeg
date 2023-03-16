package com.example.demo.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.entities.Address;
import com.example.demo.entities.Product;
import com.example.demo.entities.User;
import com.example.demo.service.contract.ICategoryService;
import com.example.demo.service.contract.IOrderDetailService;
import com.example.demo.service.contract.IOrderService;
import com.example.demo.service.contract.IProductService;
import com.example.demo.service.contract.IAddressService;
import com.example.demo.repository.entity.AddressRepository;

import java.util.Optional;

import com.example.demo.service.imp.CategoryService;
import com.example.demo.service.imp.AddressService;

import com.example.demo.service.imp.OrderDetailService;
import com.example.demo.service.imp.ProductService;


@RestController
@RequestMapping("/address")
public class AddressController {

    ModelMapper modelMapper;
    IOrderService orderService;
    IOrderDetailService orderDetailService;
    ICategoryService categoryService;
    IProductService productService;
    IAddressService addressService;


   
    public AddressController(ModelMapper modelMapper, CategoryService categoryService,AddressService addressService, OrderDetailService orderDetailService) {
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
        this.orderDetailService = orderDetailService;
        this.addressService = addressService;

    }

    @Autowired
    private AddressRepository addressRepository;

    
    @PostMapping()
    public ResponseEntity<AddressDTO> addAddress(@RequestBody AddressDTO addressDTO)
            throws BadRequest {
                Address address = modelMapper.map(addressDTO, Address.class);
        return ResponseEntity.ok(modelMapper.map(addressService.add(address), AddressDTO.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> editAddress(@PathVariable("id") long id, @RequestBody AddressDTO addressDTO)
            throws BadRequest {
        Address address = modelMapper.map(addressDTO, Address.class);
        Optional<Address> updateOptional = addressService.update(id, address);
        return updateOptional.map(c -> ResponseEntity.ok(modelMapper.map(c, AddressDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }
    

    @GetMapping("/{userId}")
    public ResponseEntity<Address> getAddress(@PathVariable("userId") long id) {
        Optional<Address> AddressOptional = addressService.findByID(id);
        return AddressOptional.map(c -> ResponseEntity.ok(modelMapper.map(c, Address.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping()
    public Boolean deleteAddress(@RequestBody Address address)
    throws BadRequest {
    Boolean result = addressRepository.existsById(address.getAddressId());
    if (result) {
    addressService.delete(address.getAddressId());
    return true;
    }
    return false;
}
}
