package com.parking.samurai.dto;

import jakarta.validation.constraints.NotBlank;

/**
* Data Transfer Object used for authentication requests.
* Carries user credentials (username and password) from the client to the authentication endpoint.
* Validation annotations ensure that required fields are not empty.
*/

public record AuthRequest(
        @NotBlank String username,
        @NotBlank String password
) {}