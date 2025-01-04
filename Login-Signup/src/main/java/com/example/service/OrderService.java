package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Order;
import com.example.entity.OrderRequest;
import com.example.repository.OrderRepository;
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public void createOrder(OrderRequest orderRequest) {
        Order order = toOrderEntity(orderRequest);
        orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    private Order toOrderEntity(OrderRequest orderRequest) {
        Order order = new Order();
        order.setProductName(orderRequest.getProductName());
        order.setQuantity(orderRequest.getQuantity());
        order.setDeliveryMode(orderRequest.getDeliveryMode());
        order.setPaymentMode(orderRequest.getPaymentMode());
        order.setAddress(orderRequest.getAddress());
        order.setContactInfo(orderRequest.getContactInfo());
        return order;
    }
}
