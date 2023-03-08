package com.example.demo.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Order;
import com.example.demo.entities.OrderDetail;
import com.example.demo.repository.RepositoryWrapper;
import com.example.demo.repository.entity.OrderRepository;
import com.example.demo.service.contract.IOrderService;

@Service
public class OrderService extends EntityService<Order, Long> implements IOrderService {

    public OrderService() {
        super(Order.class);
    }

    @Autowired
    private RepositoryWrapper repositoryWrapper;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public OrderDetail addOrderDetail(OrderDetail orderDetail) {
        boolean orderExisted = repositoryWrapper.getOrderRepository().existsById(orderDetail.getId().getOrderId());
        boolean productExisted = repositoryWrapper.getProductRepository().existsById(orderDetail.getId().getProductId());
        if (orderExisted && productExisted) {
            return repositoryWrapper.getOrderDetailRepository().save(orderDetail);
        }
        return null;
    }

    public boolean confirmOrder(Long orderId, String status) {

        boolean existsById = orderRepository.existsById(orderId);

        if (existsById) {
            Order order = orderRepository.findById(orderId).get();
            order.setStatus(1);
            orderRepository.save(order);
            return true;
        }
        return false;

    }

}
