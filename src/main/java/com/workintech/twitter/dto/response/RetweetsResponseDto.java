package com.workintech.twitter.dto.response;

import java.time.LocalDateTime;

public record RetweetsResponseDto(
        Long id,
        Long userId,
        String username,
        Long originalTweetId,
        LocalDateTime createdAt
) {
}
