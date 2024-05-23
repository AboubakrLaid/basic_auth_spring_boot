package com.example.registrationlogindemo.service;

// import org.springframework.stereotype.Service;
import com.example.registrationlogindemo.dto.OrderDto;
import com.example.registrationlogindemo.dto.NewOrderDto;
import java.util.List;
import java.util.Map;


public interface OrderService {
    // can be used by the store manager or the supplier
    List<OrderDto> getAllOrders(Long user_id);

    Map<String, Object> makeOrder(NewOrderDto order);
    // Map<String, Object> cancellOrConfirmOrder(Long id, Boolean isCancelled);
    // Map<String, Object> rejectOrAcceptOrder(Long id, Boolean isRejected);
    Map<String, Object> acceptOrder(Long id, Double taxes);
    Map<String, Object> rejectOrder(Long id);

    Map<String, Object> confirmOrder(Long id);
    Map<String, Object> cancelOrder(Long id);
}
