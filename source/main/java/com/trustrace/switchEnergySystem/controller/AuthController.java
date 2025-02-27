package com.trustrace.switchEnergySystem.controller;


import org.springframework.http.ResponseEntity;
import com.trustrace.switchEnergySystem.service.AuthService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import com.trustrace.switchEnergySystem.dto.UserDto;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDto userDto) {
        logger.info("Request to login");
        boolean loginSuccess = authService.login(userDto);
        if (loginSuccess) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        authService.register(userDto);
        logger.info("Request to register new user");
        return ResponseEntity.ok("User registered successfully");
    }
}
