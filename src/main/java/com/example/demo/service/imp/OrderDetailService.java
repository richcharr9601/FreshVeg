package com.example.demo.service.imp;

import org.springframework.stereotype.Service;

import com.example.demo.entities.OrderDetail;
import com.example.demo.entities.OrderDetailKey;
import com.example.demo.service.contract.IOrderDetailService;



@Service
public class OrderDetailService extends EntityService<OrderDetail, OrderDetailKey> implements IOrderDetailService {

    public OrderDetailService() {
        super(OrderDetail.class);
    }
}

    

