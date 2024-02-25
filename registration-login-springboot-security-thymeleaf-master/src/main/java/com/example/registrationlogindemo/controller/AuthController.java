package com.example.registrationlogindemo.controller;

import com.example.registrationlogindemo.dto.LoginDto;
import com.example.registrationlogindemo.dto.UserDto;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.service.UserService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

// import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import org.springframework.web.bind.annotation.RequestParam;


// @Controller
@RestController
@RequestMapping("/api/")
public class AuthController {
    @Autowired
    private UserService userService;

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
    public ResponseEntity<?> registration(@Valid @RequestBody UserDto user,
            BindingResult result,
            Model model) {
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors()
                    .forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

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

        userService.saveUser(user);
        response.put("message", "User registered successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("login/")
    public ResponseEntity<?> login(@RequestBody LoginDto loginRequest) {
        Map<String, Object> response = new HashMap<>();
        User user = userService.findByUsername(loginRequest.getUsername());
        if (user == null) {

            response.put("error", "Invalid email or password");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (!userService.checkIfValidOldPassword(user, loginRequest.getPassword())) {

            response.put("error", "Invalid email or password");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        response.put("message", "User logged in successfully");
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

    @GetMapping("users/")
    public ResponseEntity<?> getAllUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(userService.findAllUsers(), headers, 200);
    }
}
