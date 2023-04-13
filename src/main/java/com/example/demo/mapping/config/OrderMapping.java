package com.example.demo.mapping.config;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrderDetail;
import org.springframework.stereotype.Component;
import com.example.demo.mapping.config.contract.MappingConfig;

@Component
public class OrderMapping extends MappingConfig {

    @Override
    public void configure() {
        modelMapper.typeMap(OrderDetail.class, OrderDetailDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getProduct().getProductId(), (dest, v) -> dest.getProduct().setProductId((Long)v));
            mapper.map(src -> src.getOrder().getOrderId(), OrderDetailDTO::setOrderId);
        });

        modelMapper.typeMap(OrderDetailDTO.class, OrderDetail.class).addMappings(mapper -> {
            mapper.map(src -> src.getProduct().getProductId(), (dest, v) -> dest.getProduct().setProductId((Long)v));
        });

        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> {
            mapper.map(src -> src.getUserId(), (dest, v) -> dest.getUser().setUserId((Long)v));
        });
        
        modelMapper.typeMap(Order.class, OrderDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getUser().getUserId(), (dest, v) -> dest.setUserId((Long)v));
        });

        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> {
            mapper.map(src -> src.getAddress().getAddressId(), (dest, v) -> dest.getAddress().setAddressId((Long)v));
        });

        modelMapper.typeMap(Order.class, OrderDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getAddress().getAddressId(), (dest, v) -> dest.getAddress().setAddressId((Long)v));
        });

        modelMapper.typeMap(Order.class, OrderDTO.class).addMappings(mapper -> {
        });

        
    }

}
