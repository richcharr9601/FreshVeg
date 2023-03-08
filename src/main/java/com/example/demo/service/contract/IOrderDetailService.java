package com.example.demo.service.contract;

import org.springframework.stereotype.Service;

import com.example.demo.entities.OrderDetail;
import com.example.demo.entities.OrderDetailKey;

@Service
public interface IOrderDetailService extends IService<OrderDetail, OrderDetailKey> {
}
