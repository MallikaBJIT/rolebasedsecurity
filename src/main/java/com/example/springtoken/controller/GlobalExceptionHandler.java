package com.example.springtoken.controller;

import com.example.springtoken.response.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<?> handleBadCredentialException(BadCredentialsException ex, WebRequest webRequest) {
        return ResponseHandler.generateResponse("Check your mail or password", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<?> handleHttpMessageNotReadableException(BadCredentialsException ex, WebRequest webRequest) {
        return ResponseHandler.generateResponse("Http message not readable", HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler({CustomNotExistException.class})
//    public ResponseEntity<?> handleProductNotExistException(CustomNotExistException ex) {
//        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
//    }
}
