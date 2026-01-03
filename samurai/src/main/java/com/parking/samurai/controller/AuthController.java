package com.parking.samurai.controller;

import com.parking.samurai.dto.AuthRequest;
import com.parking.samurai.dto.AuthResponse;
import com.parking.samurai.dto.RegisterRequest;
import com.parking.samurai.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* This REST controller handles user authentication endpoints.
* Provides registration and login functionality with JWT token generation.
* Uses AuthService to encapsulate business logic for authentication and registration.
*/

@Tag(name = "Authentication", description = "User registration and login endpoint")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        // Accepts a RegisterRequest DTO and delegates registration to AuthService.
        // Returns an AuthResponse containing user info and JWT token upon successful registration.
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(summary = "Login and receive JWT token")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        // Accepts an AuthRequest DTO and delegates authentication to AuthService.
        // Returns an AuthResponse containing JWT token and user info upon successful authentication.
        return ResponseEntity.ok(authService.authenticate(request));
    }
}