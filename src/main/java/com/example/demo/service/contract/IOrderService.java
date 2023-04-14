package com.example.demo.service.contract;


import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrderDetail;
import com.example.demo.entities.Order.OrderStatus;

public interface IOrderService extends IService<Order, Long> {
    Boolean confirmOrder(Long orderId, OrderStatus status);
    Boolean orderSuccess(Long orderId, OrderStatus status);
    Boolean orderFailed(Long orderId, OrderStatus status);
    Boolean orderCancel(Long orderId, OrderStatus status);
}
