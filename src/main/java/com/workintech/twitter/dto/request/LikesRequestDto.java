package com.workintech.twitter.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record LikesRequestDto(
        @JsonProperty("user_id")
        @NotNull
        Long userId,

        @JsonProperty("tweet_id")
        @NotNull
        Long tweetId
) {
}
