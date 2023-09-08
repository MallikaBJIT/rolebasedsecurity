package com.example.springtoken.auth;

import com.example.springtoken.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
	private String name;
	private String mail;
	private String password;
	private String role;
}
