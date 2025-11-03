package com.workintech.twitter.service;

import com.workintech.twitter.dto.patchrequest.TweetsPatchRequestDto;
import com.workintech.twitter.dto.request.TweetsRequestDto;
import com.workintech.twitter.dto.response.TweetsResponseDto;

import java.util.List;

public interface TweetsService {

    List<TweetsResponseDto> getAll();
    TweetsResponseDto findById(Long id);
    TweetsResponseDto create(TweetsRequestDto tweetRequestDto);
    TweetsResponseDto replaceOrCreate(Long id, TweetsRequestDto tweetRequestDto);
    TweetsResponseDto update(Long id, TweetsPatchRequestDto tweetPatchRequestDto);
    void deleteById(Long id);
}
