package com.example.registrationlogindemo.repository;

import com.example.registrationlogindemo.dto.UserDto;
import com.example.registrationlogindemo.entity.User;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUsername(String username);
    // User findById(long id);

    // void saveUser(UserDto userDto);


    // List<UserDto> findAllUsers();
}
