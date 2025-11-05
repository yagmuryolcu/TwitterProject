package com.workintech.twitter.dto.response;

public record UserBasicInfoDto(
        Long id,
        String username,
        String fullName
) {}
