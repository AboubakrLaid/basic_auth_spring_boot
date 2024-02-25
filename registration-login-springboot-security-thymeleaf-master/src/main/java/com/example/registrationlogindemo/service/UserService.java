package com.example.registrationlogindemo.service;

import com.example.registrationlogindemo.dto.UserDto;
import com.example.registrationlogindemo.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);
    User findByUsername(String username);

    List<UserDto> findAllUsers();

    boolean checkIfValidOldPassword(User user, String password);
}