package com.workintech.twitter.controller;


import com.workintech.twitter.dto.patchrequest.UsersPatchRequestDto;
import com.workintech.twitter.dto.request.UsersRequestDto;
import com.workintech.twitter.dto.response.UsersResponseDto;
import com.workintech.twitter.entity.Users;
import com.workintech.twitter.service.UsersService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    @Autowired
    private final UsersService usersService;

    @GetMapping
    public List<UsersResponseDto> getAll(){
        return usersService.getAll();
    }

    @GetMapping("/{id}")
    public UsersResponseDto findById(@Positive @Min(1) @PathVariable ("id") Long id){
        return usersService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsersResponseDto create (@Validated @RequestBody UsersRequestDto usersRequestDto){
        return usersService.create(usersRequestDto);
    }

    @PutMapping("/{id}")
    public UsersResponseDto replaceOrCreate (@Positive @PathVariable("id") Long id , @Validated @RequestBody UsersRequestDto usersRequestDto){
        return usersService.replaceOrCreate(id,usersRequestDto);
    }

    @PatchMapping("/{id}")
    public UsersResponseDto update (@Positive @PathVariable ("id") Long id , @Validated @RequestBody UsersPatchRequestDto usersPatchRequestDto){
        return  usersService.update(id,usersPatchRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Positive @PathVariable ("id") Long id ){
        usersService.deleteById(id);
    }

}
