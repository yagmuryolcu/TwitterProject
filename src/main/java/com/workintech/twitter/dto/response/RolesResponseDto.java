package com.workintech.twitter.dto.response;

import java.util.Set;

public record RolesResponseDto(
        Long id,
        String name,
        Set<UserBasicInfoDto> users

) {
}
