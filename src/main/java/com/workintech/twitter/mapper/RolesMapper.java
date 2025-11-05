package com.workintech.twitter.mapper;


import com.workintech.twitter.dto.patchrequest.RolesPatchRequestDto;
import com.workintech.twitter.dto.request.RolesRequestDto;
import com.workintech.twitter.dto.response.RolesResponseDto;
import com.workintech.twitter.dto.response.UserBasicInfoDto;
import com.workintech.twitter.entity.Roles;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RolesMapper {

    public Roles toEntity(RolesRequestDto roleRequestDto) {
        Roles role = new Roles();
        role.setName(roleRequestDto.name());
        return role;
    }


    public RolesResponseDto toResponseDto(Roles roles) {
        Set<UserBasicInfoDto> userInfos = roles.getUsers().stream()
                .map(user -> new UserBasicInfoDto(
                        user.getId(),
                        user.getUsername(),
                        user.getFullName()
                ))
                .collect(Collectors.toSet());

        return new RolesResponseDto(
                roles.getId(),
                roles.getName(),
                userInfos
        );
    }

    public Roles updateEntity(Roles roleToUpdate, RolesPatchRequestDto rolePatchRequestDto) {
        if (rolePatchRequestDto.name() != null && !rolePatchRequestDto.name().isBlank()) {
            roleToUpdate.setName(rolePatchRequestDto.name());
        }
        return roleToUpdate;
    }
}