package com.parking.samurai.service.impl;

import com.parking.samurai.entity.User;
import com.parking.samurai.dto.AuthRequest;
import com.parking.samurai.dto.AuthResponse;
import com.parking.samurai.dto.RegisterRequest;
import com.parking.samurai.repository.UserRepository;
import com.parking.samurai.security.JwtService;
import com.parking.samurai.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
* Implementation of AuthService for handling user registration and authentication.
* Encapsulates all security logic including password hashing, authentication,
* and JWT token generation. Decouples controllers from security details.
*/

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Create a new user entity with encoded password
        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(user);

        String jwt = jwtService.generateToken(user);
        return new AuthResponse(jwt);
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        // Authenticate credentials using Spring Security AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String jwt = jwtService.generateToken(user);
        return new AuthResponse(jwt);
    }
}