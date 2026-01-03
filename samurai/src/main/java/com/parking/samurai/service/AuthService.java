package com.parking.samurai.service;

import com.parking.samurai.dto.AuthRequest;
import com.parking.samurai.dto.AuthResponse;
import com.parking.samurai.dto.RegisterRequest;

/**
* Defines authentication-related operations such as user registration and login.
* Acts as a contract for authentication business logic, decoupling controllers
* from the underlying security and persistence implementation.
*/

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse authenticate(AuthRequest request);
}