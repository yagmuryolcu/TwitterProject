package com.workintech.twitter.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record CommentsRequestDto(
        @JsonProperty("contents")
        @NotNull
        @NotBlank
        @NotEmpty
        @Size(max = 255, message = "Comment content cannot exceed 255 characters")
        String content,

        @JsonProperty("user_id")
        @NotNull
        @Positive
        Long userId,

        @JsonProperty("tweet_id")
        @NotNull
        @Positive
        Long tweetId
) {
}
