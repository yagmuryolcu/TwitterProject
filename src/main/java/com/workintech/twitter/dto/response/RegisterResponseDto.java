package com.workintech.twitter.dto.response;

public record RegisterResponseDto(
        Long id,
        String username,
        String email,
        String fullName,
        String message
) {
}
