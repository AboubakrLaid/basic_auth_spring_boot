package com.example.registrationlogindemo.controller;

import com.example.Serializer;
import com.example.registrationlogindemo.dto.LoginDto;
import com.example.registrationlogindemo.dto.UserApprovalDto;
import com.example.registrationlogindemo.dto.UserDto;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private Serializer serializer;

    private ResponseEntity<?> isAdmin(User user, LoginDto loginRequest) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "You are not authorized to view this page");
        if (user != null) {
            boolean valid = userService.checkIfValidOldPassword(user, loginRequest.getPassword());
            if (valid && !user.getRole().getName().equals("ADMIN")) {
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);

        }
        return null;
    }

    @GetMapping("/")
    public ResponseEntity<?> allUsers(@RequestBody LoginDto loginRequest) {
        // i want to make sure that the user has role ADMIN
        // if not, return 403 forbidden
        User user = userService.findByUsername(loginRequest.getUsername());
        ResponseEntity<?> isAdmin = isAdmin(user, loginRequest);
        if (isAdmin != null) {
            return isAdmin;
        }

        // endpoint to get all users
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/approve/")
    public ResponseEntity<?> approveUser(@RequestBody UserApprovalDto userApprovalDto) {
        // endpoint to approve a user
        User user = userService.findByUsername(userApprovalDto.getUsername());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User not found");
        if (user != null) {
            user.setRoleApproved(true);
            userService.approveUser(user);
            response = new HashMap<>();
            response.put("success", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
