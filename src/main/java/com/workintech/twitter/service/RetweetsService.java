package com.workintech.twitter.service;

import com.workintech.twitter.dto.request.RetweetsRequestDto;
import com.workintech.twitter.dto.response.RetweetsResponseDto;

import java.util.List;

public interface RetweetsService {

    List<RetweetsResponseDto> getAll();
    RetweetsResponseDto findById(Long id);
    List<RetweetsResponseDto> findByUserId(Long userId);
    List<RetweetsResponseDto> findByOriginalTweetId(Long tweetId);
    RetweetsResponseDto create(RetweetsRequestDto retweetsRequestDto);
    void deleteById(Long id, Long currentUserId);
}

