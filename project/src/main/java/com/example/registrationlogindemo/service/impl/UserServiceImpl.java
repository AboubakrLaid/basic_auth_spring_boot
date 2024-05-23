package com.example.registrationlogindemo.service.impl;

import com.example.registrationlogindemo.dto.UserDto;
// import com.example.registrationlogindemo.dto.ChangePasswordDto;
import com.example.registrationlogindemo.entity.Role;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.repository.RoleRepository;
import com.example.registrationlogindemo.repository.UserRepository;
import com.example.registrationlogindemo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        System.out.println("here------------------");
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        Role role = roleRepository.findByName(userDto.getRole());
        if (role == null) {
            throw new IllegalArgumentException("Role not found");
        }

        user.setRole(role);
        System.out.println("Role: " + role.getName());
        user.setRoleApproved("CLIENT".equals(userDto.getRole()));
        
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean checkIfValidOldPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }
    
    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        // remove the user with role ADMIN
        users = users.stream().filter((user) -> !"ADMIN".equals(user.getRole().getName()))
                .collect(Collectors.toList());
        // remove the password of the users
        users = users.stream().map((user) -> {
            user.setPassword("");
            return user;
        }).collect(Collectors.toList());
        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }

    private UserDto convertEntityToDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole().getName());
        userDto.setRoleApproved(user.isRoleApproved());
        return userDto;
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

    @Override
    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public void approveUser(User user) {
        user.setRoleApproved(true);
        userRepository.save(user);
    }
}
