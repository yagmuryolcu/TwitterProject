package com.workintech.twitter.dto.request;

import jakarta.validation.constraints.NotNull;

public record AssignRoleRequestDto(
        @NotNull(message = "User ID cannot be null")
        Long userId,

        @NotNull(message = "Role name cannot be null")
        String roleName
) {}
