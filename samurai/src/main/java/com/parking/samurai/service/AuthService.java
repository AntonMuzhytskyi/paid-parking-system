package com.parking.samurai.service;

import com.parking.samurai.dto.AuthRequest;
import com.parking.samurai.dto.AuthResponse;
import com.parking.samurai.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse authenticate(AuthRequest request);
}