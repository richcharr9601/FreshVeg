package com.example.demo.service.contract;


import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrderDetail;

public interface IOrderService extends IService<Order, Long> {
    OrderDTO requestOrder(Long userId, Long orderId);
    boolean confirmOrder(Long orderId, String status);

}
