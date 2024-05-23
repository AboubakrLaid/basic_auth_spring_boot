package com.example.registrationlogindemo.repository;

import com.example.registrationlogindemo.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
    
}
