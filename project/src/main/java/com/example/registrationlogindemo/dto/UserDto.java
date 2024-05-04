package com.example.registrationlogindemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto
{   
    @JsonIgnore
    private Long id;

    @NotEmpty(message = "Username should not be empty")
    private String username;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;

    // @JsonIgnore
    @NotEmpty(message = "Password should not be empty")
    private String password;

    @NotEmpty(message = "Role should not be empty")
    private String role;

    private boolean isRoleApproved;
}
