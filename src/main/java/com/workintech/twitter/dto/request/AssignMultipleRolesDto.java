package com.workintech.twitter.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record AssignMultipleRolesDto (
        @NotNull Long userId,
        @NotEmpty Set<String> roleNames
){
}
