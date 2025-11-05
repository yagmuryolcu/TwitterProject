package com.workintech.twitter.dto.patchrequest;

public record LikesPatchRequestDto(
        Long userId,
        Long tweetId,
        String username
) {
}
