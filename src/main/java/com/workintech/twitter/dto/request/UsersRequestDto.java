package com.workintech.twitter.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record UsersRequestDto(
        @JsonProperty("username")
        @NotNull
        @NotBlank
        @NotEmpty
        @Size(max = 50)
        String username,

        @JsonProperty("email")
        @Email
        @NotNull
        @NotBlank
        @NotEmpty
        @Size(max = 100)
        String email,

        @JsonProperty("password")
        @NotNull
        @NotBlank
        @NotEmpty
        @Size(max = 100)
        String password,

        @JsonProperty("full_name")
        @NotNull
        @NotBlank
        @NotEmpty
        @Size(max = 100)
        String fullName,

        @JsonProperty("bio")
        String bio) {


}
