package com.example.springtoken.exception;

import com.example.springtoken.dto.ErrorDto;
import com.example.springtoken.response.ResponseHandler;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<?> handleBadCredentialException(BadCredentialsException ex, WebRequest webRequest) {
        return ResponseHandler.generateResponse("Check your mail or password", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest webRequest) {
        return ResponseHandler.generateResponse("Http message not readable", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({SignatureException.class})
    public ResponseEntity<?> handleJwtException(SignatureException ex) {
        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String message = error.getField().toUpperCase()
                    + ": " + error.getDefaultMessage();
            errorMessage.append(message);
        });
        return ResponseHandler.generateResponse(errorMessage.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<?> handleProductNotExistException(CustomException ex) {
        return ResponseHandler.generateResponse(ex.getMessage(), ex.getStatus());
    }
}
