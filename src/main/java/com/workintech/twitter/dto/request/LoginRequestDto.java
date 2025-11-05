package com.workintech.twitter.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "Email cannot be blank")
        String usernameOrEmail,

        @NotBlank(message = "Password cannot be blank")
        String password
) {}
