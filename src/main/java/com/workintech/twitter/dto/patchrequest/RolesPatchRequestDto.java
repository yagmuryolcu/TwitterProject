package com.workintech.twitter.dto.patchrequest;

import jakarta.validation.constraints.Size;

public record RolesPatchRequestDto(
        @Size(max = 50, message = "Role name cannot exceed 50 characters")
        String name
) {
}
