package com.example.registrationlogindemo.repository;

import com.example.registrationlogindemo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long>{
    Order findById(long id);
    Order findBySupplierId(long id);
    Order findByStoreManagerId(long id);
}
