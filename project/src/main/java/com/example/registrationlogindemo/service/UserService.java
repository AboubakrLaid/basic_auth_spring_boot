package com.example.registrationlogindemo.service;

import com.example.registrationlogindemo.dto.UserDto;
// import com.example.registrationlogindemo.dto.ChangePasswordDto;
import com.example.registrationlogindemo.entity.User;

import java.util.List;

import org.h2.mvstore.tx.TransactionStore.Change;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);
    User findByUsername(String username);
    User findById(long id);

    List<UserDto> getAllUsers();

    boolean checkIfValidOldPassword(User user, String password);

    void changeUserPassword(User user, String password);

    void approveUser(User user);
}
