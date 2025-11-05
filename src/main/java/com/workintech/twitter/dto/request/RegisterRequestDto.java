package com.workintech.twitter.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
        @NotBlank(message = "Username cannot be blank")
        @Size(max = 50, message = "Username cannot exceed 50 characters")
        String username,

        @Email(message = "Email must be valid")
        @NotBlank(message = "Email cannot be blank")
        @Size(max = 100, message = "Email cannot exceed 100 characters")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        String password,

        @NotBlank(message = "Full name cannot be blank")
        @Size(max = 100, message = "Full name cannot exceed 100 characters")
        String fullName,

        String bio
) {}