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
import com.example.demo.entities.Product;
import com.example.demo.entities.User;
import com.example.demo.entities.Order.OrderStatus;
import com.example.demo.repository.entity.AddressRepository;
import com.example.demo.repository.entity.OrderRepository;
import com.example.demo.repository.entity.ProductRepository;
import com.example.demo.repository.entity.UserRepository;
import com.example.demo.service.contract.IOrderService;
import com.example.demo.service.contract.IProductService;
import com.example.demo.service.contract.IOrderDetailService;
import com.example.demo.service.imp.OrderService;
import com.example.demo.service.imp.ProductService;
import com.example.demo.service.imp.OrderDetailService;

@RestController
@RequestMapping("/order")
public class OrderController {

    ModelMapper modelMapper;
    IOrderService orderService;
    IOrderDetailService orderDetailService;
    IProductService productService;


    public OrderController(ProductService productService,ModelMapper modelMapper, OrderService orderService, OrderDetailService orderDetailService) {
        this.modelMapper = modelMapper;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.productService = productService;
    }

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;
    @GetMapping("all")
    public ResponseEntity<List<OrderDTO>> getOrders() {
        List<Order> orders = orderService.findAll();
        
        List<OrderDTO> orderDtos = modelMapper.map(orders, new TypeToken<List<OrderDTO>>() {
        }.getType());
        
        return ResponseEntity.ok(
                modelMapper.map(orderDtos, new TypeToken<List<OrderDTO>>() {
                }.getType()));
    }

    @GetMapping("{orderId}")
    public ResponseEntity<OrderDTO> getOrderByOrderId(@PathVariable("orderId") Long oid) {
        Order order = orderRepository.findByOrderId(oid);
        return ResponseEntity.ok(modelMapper.map(order, OrderDTO.class));

    }

    @GetMapping("all/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrderByOrderIdAndUserId(@PathVariable("userId") Long uid) {
        List<Order> orders = orderRepository.findByUserUserId(uid);

        List<OrderDTO> orderDtos = modelMapper.map(orders, new TypeToken<List<OrderDTO>>() {
        }.getType());
        return ResponseEntity.ok(
                modelMapper.map(orderDtos, new TypeToken<List<OrderDTO>>() {
                }.getType()));
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable("userId") Long uid) {
        List<Order> orders = orderRepository.findByUserUserId(uid);

        List<OrderDTO> orderDtos = modelMapper.map(orders, new TypeToken<List<OrderDTO>>() {
        }.getType());
        return ResponseEntity.ok(orderDtos);
    }

    // @PreAuthorize("#userId == authentication.principal.userId")
    @PostMapping()
    public ResponseEntity<OrderDTO> addOrder(@RequestBody OrderDTO orderDTO)
            throws BadRequest {
        Date date = new Date();
        Order order = modelMapper.map(orderDTO, Order.class);
        order.setOrderDate(date);
        order.setStatusPayment(false);
        order.setStatus(OrderStatus.onWaitingConfirm);
        orderService.add(order);
        order.getOrderDetails().forEach(od -> {
            orderDetailService.add(od);
        });
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            Product product = orderDetail.getProduct();
            Product product1 = productRepository.findByProductId(product.getProductId());
            int remainingWeight = product1.getWeight() - orderDetail.getWeight();
            if (remainingWeight < 0) {
                ResponseEntity.badRequest();
            }
            product1.setWeight(remainingWeight);
            product1.setCategory(product1.getCategory());
            product1.setDeleted(product1.getDeleted());
            product1.setDescription(product1.getDescription());
            product1.setDiscount(product1.getDiscount());
            product1.setEnteredDate(product1.getEnteredDate());
            product1.setPrice(product1.getPrice());
            product1.setProductId(product1.getProductId());
            product1.setProductImages(product1.getProductImages());
            product1.setProductName(product1.getProductName());
            product1.setStatus(product1.getStatus());
            productRepository.save(product1);
        }
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
        return orderService.orderSuccess(id, OrderStatus.Success) ? ResponseEntity.ok("Successfully")
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("{orderId}/failed")
    public ResponseEntity<Object> orderFailed(@PathVariable("orderId") Long id)
            throws BadRequest {              
        return orderService.orderFailed(id, OrderStatus.Failed) ? ResponseEntity.ok("Failed")
                : ResponseEntity.notFound().build();
    }
    

    @PatchMapping("{orderId}/cancel")
    public ResponseEntity<Object> orderCancel(@PathVariable("orderId") Long id)
            throws BadRequest {
                Order order = orderRepository.findByOrderId(id);
                for (OrderDetail orderDetail : order.getOrderDetails()) {
                    Product product = orderDetail.getProduct();
                    Product product1 = productRepository.findByProductId(product.getProductId());
                    int remainingWeight = product1.getWeight() + orderDetail.getWeight();
                    if (remainingWeight < 0) {
                        ResponseEntity.badRequest();
                    }
                    product1.setWeight(remainingWeight);
                    product1.setCategory(product1.getCategory());
                    product1.setDeleted(product1.getDeleted());
                    product1.setDescription(product1.getDescription());
                    product1.setDiscount(product1.getDiscount());
                    product1.setEnteredDate(product1.getEnteredDate());
                    product1.setPrice(product1.getPrice());
                    product1.setProductId(product1.getProductId());
                    product1.setProductImages(product1.getProductImages());
                    product1.setProductName(product1.getProductName());
                    product1.setStatus(product1.getStatus());
    productRepository.save(product1);}
        return orderService.orderCancel(id, OrderStatus.Cancel) ? ResponseEntity.ok("Cancel")
                : ResponseEntity.notFound().build();

}
}



