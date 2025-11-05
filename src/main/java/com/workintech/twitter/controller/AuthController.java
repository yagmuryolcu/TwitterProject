package com.workintech.twitter.controller;

import com.workintech.twitter.dto.request.LoginRequestDto;
import com.workintech.twitter.dto.request.RegisterRequestDto;
import com.workintech.twitter.dto.response.AuthResponseDto;
import com.workintech.twitter.dto.response.LoginResponseDto;
import com.workintech.twitter.dto.response.RegisterResponseDto;
import com.workintech.twitter.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthService authService;

    @PostMapping("/register")
    public RegisterResponseDto register(@Validated @RequestBody RegisterRequestDto registerRequestDto){
        return authService.register(registerRequestDto);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Validated @RequestBody LoginRequestDto loginRequestDto){
        return authService.login(loginRequestDto);
    }
}