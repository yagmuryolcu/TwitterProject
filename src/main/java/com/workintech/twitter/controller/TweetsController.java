package com.workintech.twitter.controller;


import com.workintech.twitter.dto.patchrequest.TweetsPatchRequestDto;
import com.workintech.twitter.dto.request.TweetsRequestDto;
import com.workintech.twitter.dto.response.TweetsResponseDto;
import com.workintech.twitter.service.TweetsService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tweets")
@RequiredArgsConstructor
public class TweetsController {

    @Autowired
    private final TweetsService tweetService;

    @GetMapping
    public List<TweetsResponseDto> getAll(){
        return tweetService.getAll();
    }
    @GetMapping("/{id}")
    public TweetsResponseDto findById( @Positive @Min(1) @PathVariable("id") Long id){
        return tweetService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TweetsResponseDto create (@Validated @RequestBody TweetsRequestDto tweetsRequestDto){
        return tweetService.create(tweetsRequestDto);
    }

    @PutMapping("/{id}")
    public TweetsResponseDto replaceOrCreate (@Positive @PathVariable("id") Long id , @Validated @RequestBody TweetsRequestDto tweetsRequestDto){
        return tweetService.replaceOrCreate(id,tweetsRequestDto);
    }

    @PatchMapping("/{id}")
    public TweetsResponseDto update (@Positive @PathVariable ("id") Long id , @Validated @RequestBody TweetsPatchRequestDto tweetsPatchRequestDto){
        return  tweetService.update(id,tweetsPatchRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Positive @PathVariable ("id") Long id ){
        tweetService.deleteById(id);
    }

}
