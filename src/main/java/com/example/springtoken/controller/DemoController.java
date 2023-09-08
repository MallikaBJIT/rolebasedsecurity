package com.example.springtoken.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @GetMapping("/hello_user")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("hello from secured user");
    }

    @GetMapping("/hello_admin")
    public ResponseEntity<String> helloAdmin() {
        return ResponseEntity.ok("hello from secured admin");
    }

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("welcome everyone");
    }

    @GetMapping("/adminpage")
    public ResponseEntity<String> adminpage() {
        return ResponseEntity.ok("welcome to admin page");
    }

    @GetMapping("/userpage")
    public ResponseEntity<String> userpage() {
        return ResponseEntity.ok("welcome to user page");
    }
}
