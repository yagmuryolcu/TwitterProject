package com.workintech.twitter.service;

import com.workintech.twitter.dto.patchrequest.RolesPatchRequestDto;
import com.workintech.twitter.dto.request.RolesRequestDto;
import com.workintech.twitter.dto.response.RolesResponseDto;

import java.util.List;

public interface RolesService {


    List<RolesResponseDto> getAll();
    RolesResponseDto findById(Long id);
    RolesResponseDto findByName(String name);
    RolesResponseDto create(RolesRequestDto rolesRequestDto);
    RolesResponseDto replaceOrCreate(Long id, RolesRequestDto rolesRequestDto);
    RolesResponseDto update(Long id, RolesPatchRequestDto rolesPatchRequestDto);
    void delete(Long id);
    void assignRoleToUser(Long userId, Long roleId);
    void removeRoleFromUser(Long userId, Long roleId);
}
