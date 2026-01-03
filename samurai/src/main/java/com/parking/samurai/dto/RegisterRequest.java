package com.parking.samurai.dto;

import jakarta.validation.constraints.NotBlank;

/**
* Data Transfer Object used for user registration requests.
* Carries user credentials and optional profile information.
* Validation annotations ensure required fields are provided by the client.
*/

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank String password
) {}