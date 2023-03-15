package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrderDetail;
import com.example.demo.entities.OrderDetailKey;
import com.example.demo.service.contract.IOrderService;
import com.example.demo.service.contract.IOrderDetailService;
import com.example.demo.service.imp.OrderService;
import com.example.demo.service.imp.OrderDetailService;

@RestController
@RequestMapping("/order")
public class OrderController {

    ModelMapper modelMapper;
    IOrderService orderService;
    IOrderDetailService orderDetailService;

    public OrderController(ModelMapper modelMapper, OrderService orderService, OrderDetailService orderDetailService) {
        this.modelMapper = modelMapper;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;

    }

    @GetMapping("{userId}/all")
    public ResponseEntity<List<OrderDTO>> getOrders(@PathVariable("userId") Long id) {
        List<Order> orders = orderService.findAll();
        return ResponseEntity.ok(
                modelMapper.map(orders, new TypeToken<List<OrderDTO>>() {
                }.getType()));
    }

    @GetMapping("{userId}/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable("userId") Long uid, @PathVariable("id") long id) {
        Optional<Order> userOptional = orderService.findByID(id);
        return userOptional.map(c -> ResponseEntity.ok(modelMapper.map(c, OrderDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<OrderDTO> addOrder(@RequestBody OrderDTO orderDTO)
            throws BadRequest {
        Order order = modelMapper.map(orderDTO, Order.class);
        orderService.add(order);
        return ResponseEntity.ok(modelMapper.map(order, OrderDTO.class));
    }


    // @PutMapping("{userId}/{id}")
    // public ResponseEntity<OrderDTO> editOrder(@PathVariable("userId") Long uid, @PathVariable("id") Long id,
    //         @RequestBody OrderDTO orderDTO)
    //         throws BadRequest {
    //     Order order = modelMapper.map(orderDTO, Order.class);
    //     Optional<Order> updateOptional = orderService.update(id, order);

    //     return updateOptional.map(c -> ResponseEntity.ok(modelMapper.map(c, OrderDTO.class)))
    //             .orElse(ResponseEntity.notFound().build());

    // }

    @DeleteMapping("{userId}")
    public Boolean deleteOrder(@PathVariable("userId") Long id)
            throws BadRequest {
        orderService.delete(id);
        return true;
    }



    @PatchMapping("{orderId}")
    public ResponseEntity<Object> confirmOrder(@PathVariable("orderId") long id, @RequestBody String status)
            throws BadRequest {
        return orderService.confirmOrder(id, status) ? ResponseEntity.ok("Confirmed Order")
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("order-detail")
    public Boolean deleteOrderDetail(
            @RequestBody OrderDetailKey key)
            throws BadRequest {
        orderDetailService.delete(key);
        return true;
    }

}
