package com.workintech.twitter.controller;


import com.workintech.twitter.dto.patchrequest.LikesPatchRequestDto;
import com.workintech.twitter.dto.request.LikesRequestDto;
import com.workintech.twitter.dto.response.LikesResponseDto;
import com.workintech.twitter.entity.Likes;
import com.workintech.twitter.service.LikesService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikesController {

    @Autowired
    private final LikesService likesService;

    @GetMapping
    public List<LikesResponseDto> getAll(){
        return likesService.getAll();
    }

    @GetMapping("/id")
    public LikesResponseDto findById(@Positive @PathVariable("id") Long id) {
        return likesService.findById(id);
    }

    @GetMapping("/tweet/{tweetId}")
    public List<LikesResponseDto> findByTweetId(@Positive @PathVariable("tweetId") Long tweetId) {
        return likesService.findByTweetId(tweetId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LikesResponseDto create(@Validated @RequestBody LikesRequestDto likesRequestDto) {
        return likesService.create(likesRequestDto);
    }
    @PatchMapping("/{id}")
    public LikesResponseDto update(@PathVariable("id") Long id,
                                  @RequestBody LikesPatchRequestDto likePatchRequestDto){

        return likesService.update(id, likePatchRequestDto);
    }

//tekrar dön buraya
    @DeleteMapping("/id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void dislike(
            @RequestParam @Positive Long userId,
            @RequestParam @Positive Long tweetId) {
        likesService.deleteByUserAndTweet(userId, tweetId);
    }


}
