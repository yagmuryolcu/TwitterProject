package com.workintech.twitter.service;

import com.workintech.twitter.dto.patchrequest.LikesPatchRequestDto;
import com.workintech.twitter.dto.patchrequest.TweetsPatchRequestDto;
import com.workintech.twitter.dto.request.LikesRequestDto;
import com.workintech.twitter.dto.request.TweetsRequestDto;
import com.workintech.twitter.dto.response.LikesResponseDto;
import com.workintech.twitter.dto.response.TweetsResponseDto;

import java.util.List;

public interface LikesService {
    List<LikesResponseDto> getAll();
    LikesResponseDto findById(Long id);
    List<LikesResponseDto> findByTweetId(Long tweetId);
    LikesResponseDto replaceOrCreate(Long id,LikesRequestDto likesRequestDto);
    LikesResponseDto create(LikesRequestDto likesRequestDto);
    LikesResponseDto update(Long id, LikesPatchRequestDto likesPatchRequestDto);
    void deleteByUserAndTweet(Long userId, Long tweetId);
}
