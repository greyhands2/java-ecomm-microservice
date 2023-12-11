package com.starq.orderservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.starq.orderservice.model.Order;


public interface OrderRepository extends JpaRepository<Order, Long> {
    
}