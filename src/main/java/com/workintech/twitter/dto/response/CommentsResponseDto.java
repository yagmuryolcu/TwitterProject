package com.workintech.twitter.dto.response;

import java.time.LocalDateTime;

public record CommentsResponseDto(
        Long id,
        String commentContent,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long userId,
        String username,
        Long tweetId
) {
}
