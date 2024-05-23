package com.example.registrationlogindemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.registrationlogindemo.dto.NewOrderDto;
import com.example.registrationlogindemo.service.OrderService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders(@RequestBody Map<String, Object> body) {
        try {
            Long user_id = Long.parseLong(body.get("user_id").toString());
            return new ResponseEntity<>(orderService.getAllOrders(user_id), HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/make")
    public ResponseEntity<?> makeOrder(@RequestBody NewOrderDto orderDto) {
        try {
            Map<String, Object> result = orderService.makeOrder(orderDto);
            if ((boolean) result.get("success")) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/accept")
    public ResponseEntity<?> acceptOrder(@RequestBody Map<String, Object> body) {
        try {
            Long id = Long.parseLong(body.get("id").toString());
            Double taxes = Double.parseDouble(body.get("taxes").toString());
            Map<String, Object> result = orderService.acceptOrder(id, taxes);
            if ((boolean) result.get("success")) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/reject")
    public ResponseEntity<?> rejectOrder(@RequestBody Map<String, Object> body) {
        try {
            Long id = Long.parseLong(body.get("id").toString());
            Map<String, Object> result = orderService.rejectOrder(id);
            if ((boolean) result.get("success")) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmOrder(@RequestBody Map<String, Object> body) {
        try {
            Long id = Long.parseLong(body.get("id").toString());
            Map<String, Object> result = orderService.confirmOrder(id);
            if ((boolean) result.get("success")) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelOrder(@RequestBody Map<String, Object> body) {
        try {
            Long id = Long.parseLong(body.get("id").toString());
            Map<String, Object> result = orderService.cancelOrder(id);
            if ((boolean) result.get("success")) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // @PostMapping("/accept-reject")
    // public ResponseEntity<?> acceptOrRejectOrder(@RequestBody Map<String, Object> body) {
    //     try {
    //         Long id = Long.parseLong(body.get("id").toString());
    //         Boolean isRejected = (Boolean) body.get("isRejected");
    //         Map<String, Object> result = orderService.rejectOrAcceptOrder(id, isRejected);
    //         if ((boolean) result.get("success")) {
    //             return new ResponseEntity<>(result, HttpStatus.OK);
    //         }
    //         return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    //     } catch (Exception e) {
    //         Map<String, Object> result = new HashMap<>();
    //         result.put("success", false);
    //         result.put("message", e.getMessage());
    //         return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    // @PostMapping("/confirm-cancel")
    // public ResponseEntity<?> cancelOrConfirmOrder(@RequestBody Map<String, Object> body) {
    //     try {
    //         Long id = Long.parseLong(body.get("id").toString());
    //         Boolean isCancelled = (Boolean) body.get("isCancelled");
    //         Map<String, Object> result = orderService.cancellOrConfirmOrder(id, isCancelled);
    //         if ((boolean) result.get("success")) {
    //             return new ResponseEntity<>(result, HttpStatus.OK);
    //         }
    //         return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    //     } catch (Exception e) {
    //         Map<String, Object> result = new HashMap<>();
    //         result.put("success", false);
    //         result.put("message", e.getMessage());
    //         return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

}
