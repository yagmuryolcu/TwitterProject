    package com.workintech.twitter.dto.response;

    import java.time.LocalDateTime;

    public record LikesResponseDto(
            Long id,
            Long userId,
            String username,
            Long tweetId,
            LocalDateTime likedAt
    ) {
    }
