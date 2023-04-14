package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entities.Address;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrderDetail;
import com.example.demo.entities.OrderDetailKey;
import com.example.demo.entities.Order.OrderStatus;
import com.example.demo.repository.entity.AddressRepository;
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

    @Autowired
    private AddressRepository addressRepository;

    @GetMapping("all")
    public ResponseEntity<List<OrderDTO>> getOrders() {
        List<Order> orders = orderService.findAll();
        List<OrderDTO> orderDtos = new ArrayList<>();
    
    // Iterate over each Order and create an OrderDto object with only the desired fields
    for (Order order : orders) {
        OrderDTO orderDto = new OrderDTO();
        orderDto.setOrderId(order.getOrderId());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setAmount(order.getAmount());
        orderDto.setPhone(order.getPhone());
        orderDto.setNote(order.getNote());
        orderDto.setStatusPayment(order.getStatusPayment());
        orderDto.setStatus(order.getStatus());
        
        // Check if the associated Address has been soft deleted
        Address address = addressRepository.findAddressByOrderId(order.getOrderId());
         {
            AddressDTO addressDto = new AddressDTO();
            addressDto.setAddressId(address.getAddressId());
            addressDto.setReceiverName(address.getReceiverName());
            addressDto.setReceiverPhone(address.getReceiverPhone());
            addressDto.setAddress(address.getAddress());
            addressDto.setUserId(order.getUser().getUserId());
            orderDto.setAddress(addressDto);
        }
        Set<OrderDetail> orderDetails = order.getOrderDetails();
        Set<OrderDetailDTO> orderDetailDTOs = new HashSet<>();

        for (OrderDetail orderDetail : orderDetails) {
            OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
            orderDetailDTO.setOrderId(orderDetail.getOrder().getOrderId());
            orderDetailDTO.setProduct(orderDetail.getProduct());
            orderDetailDTO.setWeight(orderDetail.getWeight());
            orderDetailDTO.setPrice(orderDetail.getPrice());
            orderDetailDTOs.add(orderDetailDTO);
        }

        orderDto.setOrderDetails(orderDetailDTOs);

        orderDto.setUserId(order.getUser().getUserId());
        
        orderDtos.add(orderDto);
    }
        return ResponseEntity.ok(
                modelMapper.map(orderDtos, new TypeToken<List<OrderDTO>>() {
                }.getType()));
    }

    @GetMapping("{orderId}")
    public ResponseEntity<OrderDTO> getOrderByOrderId( @PathVariable("orderId")Long oid) {
        Order order = orderRepository.findByOrderId(oid);
    
    
        OrderDTO orderDto = new OrderDTO();
        orderDto.setOrderId(order.getOrderId());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setAmount(order.getAmount());
        orderDto.setPhone(order.getPhone());
        orderDto.setNote(order.getNote());
        orderDto.setStatusPayment(order.getStatusPayment());
        orderDto.setStatus(order.getStatus());
        
        Address address = addressRepository.findAddressByOrderId(order.getOrderId());
         {
            AddressDTO addressDto = new AddressDTO();
            addressDto.setAddressId(address.getAddressId());
            addressDto.setReceiverName(address.getReceiverName());
            addressDto.setReceiverPhone(address.getReceiverPhone());
            addressDto.setAddress(address.getAddress());
            addressDto.setUserId(order.getUser().getUserId());
            orderDto.setAddress(addressDto);
        }
        Set<OrderDetail> orderDetails = order.getOrderDetails();
        Set<OrderDetailDTO> orderDetailDTOs = new HashSet<>();

        for (OrderDetail orderDetail : orderDetails) {
            OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
            orderDetailDTO.setOrderId(orderDetail.getOrder().getOrderId());
            orderDetailDTO.setProduct(orderDetail.getProduct());
            orderDetailDTO.setWeight(orderDetail.getWeight());
            orderDetailDTO.setPrice(orderDetail.getPrice());
            orderDetailDTOs.add(orderDetailDTO);
        }

        orderDto.setOrderDetails(orderDetailDTOs);

        orderDto.setUserId(order.getUser().getUserId());

    return ResponseEntity.ok(modelMapper.map(orderDto, OrderDTO.class));
  
    }

    // @PreAuthorize("#userId == authentication.principal.userId")
    @PostMapping()
    public ResponseEntity<OrderDTO> addOrder(@RequestBody OrderDTO orderDTO)
            throws BadRequest {

        long unixTime = System.currentTimeMillis() / 1000L;
        Order order = modelMapper.map(orderDTO, Order.class);
        order.setOrderDate(unixTime);
        order.setStatusPayment(false);
        order.setStatus(OrderStatus.onWaitingConfirm);
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
    return ResponseEntity.ok("Order with ID" + order.getOrderId() + " has been deleted");
    }
    return ResponseEntity.ok("Cannot delete");
}


    @PatchMapping("{orderId}/confirmed")
    public ResponseEntity<Object> confirmOrder(@PathVariable("orderId") Long id)
            throws BadRequest {
        return orderService.confirmOrder(id, OrderStatus.Confirmed) ? ResponseEntity.ok("Confirmed Order")
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("{orderId}/success")
    public ResponseEntity<Object> orderSuccess(@PathVariable("orderId") Long id)
            throws BadRequest {
        return orderService.confirmOrder(id, OrderStatus.Success) ? ResponseEntity.ok("Successfully")
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("{orderId}/failed")
    public ResponseEntity<Object> orderFailed(@PathVariable("orderId") Long id)
            throws BadRequest {
        return orderService.confirmOrder(id, OrderStatus.Failed) ? ResponseEntity.ok("Failed")
                : ResponseEntity.notFound().build();
    }

}

