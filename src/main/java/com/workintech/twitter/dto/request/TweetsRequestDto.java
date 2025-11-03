package com.workintech.twitter.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TweetsRequestDto(
        @JsonProperty("contents")
        @NotBlank
        @NotEmpty
        @NotNull
        @Size(max = 100, message = "Tweet content cannot exceed 100 characters")
        String contents,

        @JsonProperty("userId")
        @NotNull
        Long userId
) {
}
