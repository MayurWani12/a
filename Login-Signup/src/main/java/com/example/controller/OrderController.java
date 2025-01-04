package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Order;
import com.example.entity.OrderRequest;
import com.example.repository.OrderRepository;
import com.example.service.OrderService;

import jakarta.validation.Valid;



    @RestController
    @RequestMapping("api/orders")
    public class OrderController {
        @Autowired
        private OrderService orderService;

        private OrderRepository orderRepository;
        
        @GetMapping
        public List<Order> getAllOrders() {
            List<Order> orders = orderService.getAllOrders();
            System.out.println("Fetched orders: " + orders);
            return orders;
        }

        
        @PostMapping
        public ResponseEntity<String> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
            try {
                orderService.createOrder(orderRequest); 
                return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating order: " + e.getMessage());
            }
        }

    }


