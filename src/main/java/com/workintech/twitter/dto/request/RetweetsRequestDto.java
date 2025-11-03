package com.workintech.twitter.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RetweetsRequestDto (
        @JsonProperty("user_id")
        @NotNull
        @Positive
        Long userId,

        @JsonProperty("original_tweet_id")
        @NotNull
        @Positive
        Long originalTweetId
){
}
