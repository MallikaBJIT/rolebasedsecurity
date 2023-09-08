package com.example.springtoken.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    @NotNull(message = "Name is required")
    private String name;
    @Email(message = "Invalid email")
    private String mail;
    @NotNull(message = "Password is required")
    private String password;
    @NotNull(message = "Role is required")
    private String role;
}
