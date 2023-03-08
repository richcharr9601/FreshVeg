package com.example.demo.service.contract;


import com.example.demo.entities.Order;
import com.example.demo.entities.OrderDetail;

public interface IOrderService extends IService<Order, Long> {
    OrderDetail addOrderDetail(OrderDetail orderDetail);
    boolean confirmOrder(Long orderId, String status);

}
