package com.workintech.twitter.dto.response;

import java.time.LocalDateTime;

public record TweetsResponseDto(
        Long tweetId,
        String contents,
        LocalDateTime createdAt,
        Long userId,
        String username

) {
}
