package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.example.demo.repository.entity.OrderRepository;
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

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("all")
    public ResponseEntity<List<OrderDTO>> getOrders() {
        List<Order> orders = orderService.findAll();
        return ResponseEntity.ok(
                modelMapper.map(orders, new TypeToken<List<OrderDTO>>() {
                }.getType()));
    }

    // @GetMapping("{userId}/all")
    // public ResponseEntity<List<OrderDTO>> getOrders(@PathVariable("userId") Long id) {
    //     List<Order> orders = orderService.findAll();
    //     return ResponseEntity.ok(
    //             modelMapper.map(orders, new TypeToken<List<OrderDTO>>() {
    //             }.getType()));
    // }

    // @GetMapping("{userId}/{id}")
    // public ResponseEntity<OrderDTO> getOrder(@PathVariable("userId") Long uid, @PathVariable("id") long id) {
    //     Optional<Order> userOptional = orderService.findByID(id);
    //     return userOptional.map(c -> ResponseEntity.ok(modelMapper.map(c, OrderDTO.class)))
    //             .orElse(ResponseEntity.notFound().build());
    // }

    // @PreAuthorize("#userId == authentication.principal.userId")
    @PostMapping()
    public ResponseEntity<OrderDTO> addOrder(@RequestBody OrderDTO orderDTO)
            throws BadRequest {
        Order order = modelMapper.map(orderDTO, Order.class);
        orderService.add(order);

        order.getOrderDetails().forEach(od -> {
            orderDetailService.add(od);
        });
        
        return ResponseEntity.ok(modelMapper.map(order, OrderDTO.class));
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteOrder(@RequestBody Order order)
    throws BadRequest {
    Boolean result = orderRepository.existsById(order.getOrderId());
    if (result) {
        orderRepository.deleteById(order.getOrderId());
    return ResponseEntity.ok("Order with ID" + order.getOrderId() + "has been deleted");
    }
    return ResponseEntity.ok("Cannot delete");
}


    @PatchMapping("{orderId}")
    public ResponseEntity<Object> confirmOrder(@PathVariable("orderId") long id, @RequestBody String status)
            throws BadRequest {
        return orderService.confirmOrder(id, status) ? ResponseEntity.ok("Confirmed Order")
                : ResponseEntity.notFound().build();
    }

}

