package com.workintech.twitter.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

public record UsersResponseDto(
        Long id,
        String username,
        String email,
        String fullName,
        String bio,
        LocalDateTime createdAt,
        Set<String> roles
) {
}
