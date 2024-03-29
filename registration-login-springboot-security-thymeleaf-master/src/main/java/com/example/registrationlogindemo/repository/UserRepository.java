package com.example.registrationlogindemo.repository;

import com.example.registrationlogindemo.dto.UserDto;
import com.example.registrationlogindemo.entity.User;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUsername(String username);

    // void saveUser(UserDto userDto);


    // List<UserDto> findAllUsers();
}
