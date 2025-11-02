package com.workintech.twitter.dto.patchrequest;

public record UsersPatchRequestDto(
        String username,
        String email,
        String password,
        String fullName,
        String bio
) {
}
