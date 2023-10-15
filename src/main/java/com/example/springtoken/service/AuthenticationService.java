package com.example.springtoken.service;

import com.example.springtoken.auth.AuthenticationRequest;
import com.example.springtoken.auth.AuthenticationResponse;
import com.example.springtoken.dto.UserRequestDto;
import com.example.springtoken.exception.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.springtoken.entity.Role;
import com.example.springtoken.entity.User;
import com.example.springtoken.repository.UserRepository;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(UserRequestDto request) {
        boolean isPresent = userRepository.findByEmail(request.getEmail()).isPresent();
        if (isPresent) {
            throw new CustomException("Mail should be unique", HttpStatus.BAD_REQUEST);
        }
        var user = User.builder().name(request.getName()).email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(request
                        .getRoles()
                        .stream()
                        .map(Role::valueOf)
                        .collect(Collectors.toSet()))
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request)
            throws HttpMessageNotReadableException, BadCredentialsException {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

}
