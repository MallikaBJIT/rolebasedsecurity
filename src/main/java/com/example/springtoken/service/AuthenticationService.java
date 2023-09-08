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

@Service
@AllArgsConstructor
public class AuthenticationService {
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(UserRequestDto request) {
        boolean isPresent = userRepository.findByMail(request.getMail()).isPresent();
        if (isPresent) {
            throw new CustomException("Mail should be unique", HttpStatus.BAD_REQUEST);
        }
        var user = User.builder().name(request.getName()).mail(request.getMail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole())).build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws HttpMessageNotReadableException, BadCredentialsException {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getMail(), request.getPassword()));
        var user = userRepository.findByMail(request.getMail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

}
