package com.workintech.twitter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RolesRequestDto(
        @NotBlank
        @NotEmpty
        @NotNull
        @Size(max = 50, message = "Role name cannot exceed 50 characters")
        String name) {
}
