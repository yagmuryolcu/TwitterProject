package com.workintech.twitter.service;

import com.workintech.twitter.dto.patchrequest.UsersPatchRequestDto;
import com.workintech.twitter.dto.request.UsersRequestDto;
import com.workintech.twitter.dto.response.UsersResponseDto;
import com.workintech.twitter.entity.Users;

import java.util.List;

public interface UsersService {

    List<UsersResponseDto> getAll();
    UsersResponseDto findById(Long id);
    UsersResponseDto create(UsersRequestDto userRequestDto);
    UsersResponseDto replaceOrCreate(Long id, UsersRequestDto userRequestDto);
    UsersResponseDto update(Long id, UsersPatchRequestDto userPatchRequestDto);
    void deleteById(Long id);
    void assignRoleToUser(Long userId, String roleName);
    void removeRoleFromUser(Long userId, String roleName);
    UsersResponseDto getUserRoles(Long userId);
}
