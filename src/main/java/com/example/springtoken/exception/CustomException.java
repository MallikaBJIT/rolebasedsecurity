package com.example.springtoken.exception;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    private String message;
    @Getter
    private HttpStatus status;

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
