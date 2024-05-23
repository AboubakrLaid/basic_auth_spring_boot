package com.example.registrationlogindemo.controller;

import com.example.Serializer;
import com.example.registrationlogindemo.dto.ChangePasswordDto;
import com.example.registrationlogindemo.dto.LoginDto;
import com.example.registrationlogindemo.dto.UserDto;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import java.util.Collections;


// @Controller
@RestController
@RequestMapping("/api/")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private Serializer serializer;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<?> index() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>("{\"message\": \"Welcome to the Registration and Login API!\"}", headers, 200);
    }

    // handler method to handle register user form submit request
    @PostMapping("register/")
    public ResponseEntity<?> registration(@RequestBody UserDto user) {

        ResponseEntity<?> x = serializer.serialize(user, "UserDto");
        System.out.println("Errors" + x);
        if (x != null) {
            return x;
        }
        Map<String, Object> response = new HashMap<>();
        // if (result.hasErrors()) {
        //     Map<String, String> errors = new HashMap<>();
        //     result.getFieldErrors()
        //             .forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
        //     response.put("errors", errors);
        //     return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        // }

        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            // Email already exists

            Map<String, List<String>> errors = new HashMap<>();
            errors.put("email", Collections.singletonList("email already used"));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        User existing1 = userService.findByUsername(user.getUsername());
        if (existing1 != null) {
            // Email already exists

            Map<String, List<String>> errors = new HashMap<>();
            errors.put("username", Collections.singletonList("username already used"));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try {
            userService.saveUser(user);
            response.put("message", "User registered successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, List<String>> errors = new HashMap<>();

            errors.put("role", Collections.singletonList("role not found"));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, List<String>> errors = new HashMap<>();

            errors.put("error", Collections.singletonList("An error occurred"));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        
    }

    @PostMapping("login/")
    public ResponseEntity<?> login( @RequestBody LoginDto loginRequest) {

        ResponseEntity<?> x = serializer.serialize(loginRequest, "loginRequest");
        if (x != null) {
            return x;
        }

        Map<String, Object> response = new HashMap<>();
        User user = userService.findByUsername(loginRequest.getUsername());
        if (user == null) {

            response.put("error", "Invalid email or password");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (!userService.checkIfValidOldPassword(user, loginRequest.getPassword())) {

            response.put("error", "Invalid username or password");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (!user.isRoleApproved()) {
            response.put("is_role_approved", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        response.put("role", user.getRole().getName());
        response.put("id", user.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("logout/")
    public ResponseEntity<?> logout(@RequestBody LoginDto loginRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.findByUsername(loginRequest.getUsername());
            if (user == null) {

                response.put("error", "Invalid email or password");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            if (!userService.checkIfValidOldPassword(user, loginRequest.getPassword())) {

                response.put("error", "Invalid email or password");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            response.put("message", "User logged out successfully");
            System.out.println(response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", "An error occurred during logout");
            System.out.println(response);

            return ResponseEntity.badRequest().body(response);
        }

    }

    


    @PostMapping("change-password/")
    public ResponseEntity<?> changePassword( @RequestBody ChangePasswordDto changePasswordDto) {
        
        ResponseEntity<?> x = serializer.serialize(changePasswordDto, "changePasswordDto");
        if (x != null) {
            return x;
        }

        Map<String, Object> response = new HashMap<>();
        User user = userService.findByUsername(changePasswordDto.getUsername());

        System.out.println("Received: " + changePasswordDto);

        if (user == null) {
            response.put("error", "User not found");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (!userService.checkIfValidOldPassword(user, changePasswordDto.getOldPassword())) {
            response.put("error", "Invalid old password");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        userService.changeUserPassword(user, changePasswordDto.getNewPassword());
        response.put("message", "Password changed successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
