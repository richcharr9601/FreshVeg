package com.example.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.example.demo.dto.AddressDTO;
import com.example.demo.entities.Address;
import com.example.demo.entities.User;
import com.example.demo.repository.entity.AddressRepository;
import com.example.demo.repository.entity.UserRepository;
import com.example.demo.security.service.JwtService;
import com.example.demo.service.contract.IAddressService;
import com.example.demo.service.contract.ICategoryService;
import com.example.demo.service.contract.IOrderDetailService;
import com.example.demo.service.contract.IOrderService;
import com.example.demo.service.contract.IProductService;
import com.example.demo.service.imp.AddressService;
import com.example.demo.service.imp.CategoryService;
import com.example.demo.service.imp.OrderDetailService;
import com.example.demo.service.imp.OrderService;
import com.example.demo.service.imp.ProductService;
import com.example.demo.service.imp.UserService;


import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    final ModelMapper modelMapper;
    
    private final JwtService jwtService;

    private final AddressService addressService;

    private final OrderDetailService orderDetailService;

    private final CategoryService categoryService;

    private final ProductService productService;

    private final OrderService orderService;

    private final UserService userService;

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;


    // @PreAuthorize("#userId == authentication.principal.userId")
    @PostMapping("/{userId}")
    public ResponseEntity<AddressDTO> addAddress(@PathVariable("userId") Long userId, @RequestBody AddressDTO addressDTO)
            throws BadRequest {
        boolean exist = userService.existsByID(userId);

                
        if(exist) {
            Address address = modelMapper.map(addressDTO, Address.class);
            User user = new User();
            user.setUserId(userId);
            address.setUser(user);
            
            return ResponseEntity.ok(modelMapper.map(addressService.add(address), AddressDTO.class));
        } else return ResponseEntity.notFound().build();
    }

    // @PreAuthorize("#userId == authentication.principal.userId")
    @PutMapping("/{userId}/{addressId}")
    public ResponseEntity<AddressDTO> editAddress(@PathVariable("addressId") Long aid,@PathVariable("userId") Long userId, @RequestBody AddressDTO addressDTO)
            throws BadRequest {               
        Address address = modelMapper.map(addressDTO, Address.class);
        User user = userRepository.findByUserId(userId);
        user.setUserId(userId);
        address.setUser(user);
        Optional<Address> updateOptional = addressService.update(aid, address);
        return updateOptional.map(c -> ResponseEntity.ok(modelMapper.map(c, AddressDTO.class)))
            .orElse(ResponseEntity.notFound().build());
}
    
    // @PreAuthorize("#userId == authentication.principal.userId")
    @GetMapping("/{userId}")
    public ResponseEntity<Set<Address>> getAddress(@PathVariable("userId") long userId) {
        
        Optional<User> user = userService.findByID(userId);
        return ResponseEntity.ok(addressRepository.findByUser(user));
    }


    // @PreAuthorize("#userId == authentication.principal.userId")
    @DeleteMapping("/{userId}/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable("userId") Long userId, @PathVariable("addressId") Long addressId)
    throws BadRequest {
    Boolean result = addressRepository.existsById(addressId);
    Address address = addressRepository.findByAddressId(addressId);
    if (result) {
        User user = userRepository.findByUserId(userId);
        user.setUserId(userId);
        address.setUser(user);
    addressRepository.deleteById(addressId);
    return ResponseEntity.ok("Address with ID " + address.getAddressId() + " has been deleted");
    }
    return ResponseEntity.ok("Cannot delete");
}
}
