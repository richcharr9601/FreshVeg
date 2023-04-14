package com.example.demo.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrderDetail;
import com.example.demo.entities.Order.OrderStatus;
import com.example.demo.repository.RepositoryWrapper;
import com.example.demo.repository.entity.OrderRepository;
import com.example.demo.service.contract.IOrderService;

@Service
public class OrderService extends EntityService<Order, Long> implements IOrderService {

    public OrderService() {
        super(Order.class);
    }


    @Autowired
    private OrderRepository orderRepository;


    

    public Boolean confirmOrder(Long orderId, OrderStatus status) {

        boolean existsById = orderRepository.existsById(orderId);

        if (existsById) {
            Order order = orderRepository.findById(orderId).get();
            order.setStatus(OrderStatus.Confirmed);
            orderRepository.save(order);
            return true;
        }
        return false;

    }

    public Boolean orderSuccess(Long orderId, OrderStatus status) {

        boolean existsById = orderRepository.existsById(orderId);

        if (existsById) {
            Order order = orderRepository.findById(orderId).get();
            order.setStatus(OrderStatus.Success);
            orderRepository.save(order);
            return true;
        }
        return false;
    }

    public Boolean orderFailed(Long orderId, OrderStatus status) {

        boolean existsById = orderRepository.existsById(orderId);

        if (existsById) {
            Order order = orderRepository.findById(orderId).get();
            order.setStatus(OrderStatus.Failed);
            orderRepository.save(order);
            return true;
        }
        return false;
    }

    public Boolean orderCancel(Long orderId, OrderStatus status) {

        boolean existsById = orderRepository.existsById(orderId);

        if (existsById) {
            Order order = orderRepository.findById(orderId).get();
            order.setStatus(OrderStatus.Cancel);
            orderRepository.save(order);
            return true;
        }
        return false;
    }

}
