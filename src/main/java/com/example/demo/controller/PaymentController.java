package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.entities.Product;
import com.example.demo.service.imp.OrderService;

public class PaymentController {

    // ModelMapper modelMapper;
    // IOrderService orderService;
    // IUserService userService;

    // public OrderController(ModelMapper modelMapper, OrderService orderService, UserService userService) {
    //     this.modelMapper = modelMapper;
    //     this.orderService = orderService;
    //     this.userService = userService;

    // }
    // @PostMapping("create-payment")
    // public ResponseEntity<?> createPayment(@RequestBody PaymentDTO paymentDTO)
    //         throws BadRequest {
    //         User user = (User) this.userService.getCurrentUserLogin();
    //         OrderDTO orderDTO = orderService.orderRequest(user.getId(), requestParams.getIdServerPack());
    // }
}
