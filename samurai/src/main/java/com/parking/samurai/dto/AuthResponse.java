package com.parking.samurai.dto;

/**
* Data Transfer Object used as a response for authentication operations.
* Contains a JWT token that is returned to the client after successful login or registration.
* The token is used by the client to authorize subsequent requests.
*/

public record AuthResponse(String token) {}