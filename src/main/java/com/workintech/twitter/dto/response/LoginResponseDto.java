package com.workintech.twitter.dto.response;

public record LoginResponseDto(
        Long id,
        String username,
        String email,
        String message
) {
}
