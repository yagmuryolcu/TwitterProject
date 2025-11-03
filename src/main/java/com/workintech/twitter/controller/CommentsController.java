package com.workintech.twitter.controller;

import com.workintech.twitter.dto.patchrequest.CommentsPatchRequestDto;
import com.workintech.twitter.dto.request.CommentsRequestDto;
import com.workintech.twitter.dto.response.CommentsResponseDto;
import com.workintech.twitter.service.CommentsService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentsController {

    @Autowired
    private final CommentsService commentsService;

    @GetMapping
    public List<CommentsResponseDto> getAll() {
        return commentsService.getAll();
    }

    @GetMapping("/{id}")
    public CommentsResponseDto findById(@Positive @PathVariable("id") Long id) {
        return commentsService.findById(id);
    }

    @GetMapping("/tweet/{tweetId}")
    public List<CommentsResponseDto> findByTweetId(@Positive @PathVariable("tweetId") Long tweetId) {
        return commentsService.findByTweetId(tweetId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentsResponseDto create(@Validated @RequestBody CommentsRequestDto commentsRequestDto) {
        return commentsService.create(commentsRequestDto);
    }

    @PutMapping("/{id}")
    public CommentsResponseDto update(
            @Positive @PathVariable("id") Long id,
            @Validated @RequestBody CommentsPatchRequestDto commentsPatchRequestDto,
            @RequestParam @Positive Long currentUserId) {
        return commentsService.update(id, commentsPatchRequestDto, currentUserId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Positive @PathVariable("id") Long id,
            @RequestParam @Positive Long currentUserId,
            @RequestParam @Positive Long tweetOwnerId) {
        commentsService.deleteById(id, currentUserId, tweetOwnerId);
    }


}
