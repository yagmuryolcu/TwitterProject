package com.workintech.twitter.dto.response;

public record AuthResponseDto(
        String token,
        String type,
        Long userId,
        String username,
        String email
) {
    public AuthResponseDto(String token, Long userId, String username, String email) {
        this(token, "Bearer", userId, username, email);
    }
}