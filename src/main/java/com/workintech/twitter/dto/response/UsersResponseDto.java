package com.workintech.twitter.dto.response;

import java.time.LocalDateTime;

public record UsersResponseDto(

        String username,
        String email,
        String fullName,
        String bio
) {
}
