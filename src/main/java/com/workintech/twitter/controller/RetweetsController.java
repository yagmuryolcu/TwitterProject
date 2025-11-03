package com.workintech.twitter.controller;


import com.workintech.twitter.dto.request.RetweetsRequestDto;
import com.workintech.twitter.dto.response.RetweetsResponseDto;
import com.workintech.twitter.service.RetweetsService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/retweet")
@RequiredArgsConstructor
public class RetweetsController {

    @Autowired
    private final RetweetsService retweetsService;


    @GetMapping
    public List<RetweetsResponseDto> getAll() {
        return retweetsService.getAll();
    }

    @GetMapping("/{id}")
    public RetweetsResponseDto findById(@Positive @PathVariable("id") Long id) {
        return retweetsService.findById(id);
    }

    @GetMapping("/user/{userId}")
    public List<RetweetsResponseDto> findByUserId(@Positive @PathVariable("userId") Long userId) {
        return retweetsService.findByUserId(userId);
    }

    @GetMapping("/tweet/{tweetId}")
    public List<RetweetsResponseDto> findByOriginalTweetId(@Positive @PathVariable("tweetId") Long tweetId) {
        return retweetsService.findByOriginalTweetId(tweetId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RetweetsResponseDto create(@Validated @RequestBody RetweetsRequestDto retweetsRequestDto) {
        return retweetsService.create(retweetsRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Positive @PathVariable("id") Long id,
            @RequestParam @Positive Long currentUserId) {
        retweetsService.deleteById(id, currentUserId);
    }

}
