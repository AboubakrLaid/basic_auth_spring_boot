package com.example.registrationlogindemo.service.impl;

import com.example.registrationlogindemo.dto.NewOrderDto;
import com.example.registrationlogindemo.dto.StoreManagerDto;
import com.example.registrationlogindemo.dto.SupplierDto;
import com.example.registrationlogindemo.entity.Order;
import com.example.registrationlogindemo.service.OrderService;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.registrationlogindemo.repository.ArticleRepository;
import com.example.registrationlogindemo.repository.OrderItemRepository;
import com.example.registrationlogindemo.repository.OrderRepository;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.service.UserService;
import com.example.registrationlogindemo.entity.OrderItem;
import com.example.registrationlogindemo.dto.NewOrderItemDto;
import java.util.Map;
import java.util.HashMap;
import com.example.registrationlogindemo.dto.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<OrderDto> getAllOrders(Long user_id) {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStoreManager().getId() == user_id || order.getSupplier().getId() == user_id) {
                OrderDto orderDto = new OrderDto();
                orderDto.setId(order.getId());
                orderDto.setCreatedAt(order.getCreatedAt());
                orderDto.setStatus(order.getStatus().toString());
                orderDto.setTotal(order.getTotal());
                orderDto.setTaxes(order.getTaxes());

                SupplierDto supplierDto = new SupplierDto();
                supplierDto.setId(order.getSupplier().getId());
                supplierDto.setUsername(order.getSupplier().getUsername());
                orderDto.setSupplier(supplierDto);

                StoreManagerDto storeManagerDto = new StoreManagerDto();
                storeManagerDto.setId(order.getStoreManager().getId());
                storeManagerDto.setUsername(order.getStoreManager().getUsername());
                orderDto.setStoreManager(storeManagerDto);

                // the items
                List<OrderItem> items = order.getItems();
                List<OrderItemDto> itemDtos = new ArrayList<>();
                for (OrderItem item : items) {
                    OrderItemDto itemDto = new OrderItemDto();
                    itemDto.setId(item.getId());
                    itemDto.setQuantity(item.getQuantity());
                    
                    ArticleDto articleDto = new ArticleDto();
                    articleDto.setId(item.getArticle().getId());
                    articleDto.setTitle(item.getArticle().getTitle());
                    articleDto.setQuantity(item.getArticle().getQuantity());
                    articleDto.setPrice(item.getArticle().getPrice());
                    articleDto.setDescription(item.getArticle().getDescription());
                    articleDto.setCreatedAt(item.getArticle().getCreatedAt());
                    articleDto.setExpirationDate(item.getArticle().getExpirationDate());
                    itemDto.setArticle(articleDto);
                    itemDtos.add(itemDto);
                }
                orderDto.setItems(itemDtos);
                orderDtos.add(orderDto);
            }
        }
        return orderDtos;


    }

    @Override
    public Map<String, Object> makeOrder(NewOrderDto order) {
        User supplier = userService.findByUsername(order.getSupplier().getUsername());
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        if (supplier == null) {
            response.put("message", "Supplier not found");
            return response;
        }
        if (!supplier.getRole().getName().equals(User.ROLE.SUPPLIER.toString())) {
            response.put("message", "User is not a supplier");
            return response;
        }
        User storeManager = userService.findByUsername(order.getStoreManager().getUsername());
        if (storeManager == null) {
            response.put("message", "Store manager not found");
            return response;
        }
        if (!storeManager.getRole().getName().equals(User.ROLE.STORE_MANAGER.toString())) {
            response.put("message", "User is not a store manager");
            return response;
        }
        Order newOrder = new Order();
        newOrder.setSupplier(supplier);
        newOrder.setStoreManager(storeManager);
        newOrder.setCreatedAt(order.getCreatedAt());
        newOrder.setStatus(Order.Status.PENDING);

        List<OrderItem> items = new ArrayList<>();
        for (NewOrderItemDto itemDto : order.getItems()) {
            // need to verify that the article belongs to the supplier
            if (articleRepository.findById(itemDto.getArticleId()).get().getSupplier().getId() != supplier.getId()) {
                response.put("success", false);
                response.put("message", "Article does not belong to the supplier");
                return response;
            }
            items.add(itemDto.ConvertToOrderItem(articleRepository));
            System.out.println("items" + items);
        }
        newOrder.setItems(items);
        Boolean is_created = orderRepository.save(newOrder) != null;
        response.put("success", is_created);
        if (!is_created) {
            response.put("message", "Failed to create order");
        } else {
            System.out.println("Order created successfully");
            for (OrderItem item : items) {
                System.out.println("Order item: " + item.getId());
                item.setOrder(newOrder);
                orderItemRepository.save(item);
            }
            response.put("message", "Order created successfully");
        }

        return response;

    }

    @Override
    public Map<String, Object> acceptOrder(Long id, Double taxes) {
        // get the order
        Order order = orderRepository.findById(id).get();
        Map<String, Object> response = new HashMap<>();
        if (order == null) {
            response.put("success", false);
            response.put("message", "Order not found");
            return response;
        }
        order.setStatus(Order.Status.ACCEPTED);
        order.setTaxes(taxes);

        // calculate the total

        List<OrderItem> items = order.getItems();
        Double total = 0.0;
        for (OrderItem item : items) {
            total += Double.parseDouble(String.format("%.2f", item.getArticle().getPrice() * item.getQuantity()));
            System.out.println("total "+ total);
            // update the article quantity
            item.getArticle().setQuantity(item.getArticle().getQuantity() - item.getQuantity());
        }
        total = total + (Double.parseDouble(String.format("%.2f", total * taxes)));
        order.setTotal(total);

        orderRepository.save(order);
        response.put("success", true);
        response.put("message", "Order accepted successfully");
        return response;
    }

    @Override
    public Map<String, Object> rejectOrder(Long id) {
        // get the order
        Order order = orderRepository.findById(id).get();
        Map<String, Object> response = new HashMap<>();
        if (order == null) {
            response.put("success", false);
            response.put("message", "Order not found");
            return response;
        }
        order.setStatus(Order.Status.REJECTED);
        orderRepository.save(order);
        response.put("success", true);
        response.put("message", "Order rejected successfully");
        return response;
    }

    @Override
    public Map<String, Object> confirmOrder(Long id) {
        // get the order
        Order order = orderRepository.findById(id).get();
        Map<String, Object> response = new HashMap<>();
        if (order == null) {
            response.put("success", false);
            response.put("message", "Order not found");
            return response;
        }
        order.setStatus(Order.Status.CONFIRMED);
        orderRepository.save(order);
        response.put("success", true);
        response.put("message", "Order confirmed successfully");
        return response;
    }

    @Override
    public Map<String, Object> cancelOrder(Long id) {
        // get the order
        Order order = orderRepository.findById(id).get();
        Map<String, Object> response = new HashMap<>();
        if (order == null) {
            response.put("success", false);
            response.put("message", "Order not found");
            return response;
        }
        order.setStatus(Order.Status.CANCELLED);
        orderRepository.save(order);
        response.put("success", true);
        response.put("message", "Order cancelled successfully");
        return response;
    }

    // @Override
    // public Map<String, Object> cancellOrConfirmOrder(Long id, Boolean
    // isCancelled) {
    // // get the order
    // Order order = orderRepository.findById(id).get();
    // Map<String, Object> response = new HashMap<>();
    // if (order == null) {
    // response.put("success", false);
    // response.put("message", "Order not found");
    // return response;
    // }
    // if (isCancelled) {
    // order.setStatus(Order.Status.CANCELLED);
    // } else {
    // order.setStatus(Order.Status.CONFIRMED);
    // }
    // orderRepository.save(order);
    // response.put("success", true);
    // response.put("message", "Order " + (isCancelled ? "cancelled" : "confirmed")
    // + " successfully");
    // return response;
    // }

    // @Override
    // public Map<String, Object> rejectOrAcceptOrder(Long id, Boolean isRejected) {
    // // get the order
    // Order order = orderRepository.findById(id).get();
    // Map<String, Object> response = new HashMap<>();
    // if (order == null) {
    // response.put("success", false);
    // response.put("message", "Order not found");
    // return response;
    // }
    // if (isRejected) {
    // order.setStatus(Order.Status.REJECTED);
    // } else {
    // order.setStatus(Order.Status.ACCEPTED);
    // }
    // orderRepository.save(order);
    // response.put("success", true);
    // response.put("message", "Order " + (isRejected ? "rejected" : "accepted") + "
    // successfully");
    // return response;
    // }

}
