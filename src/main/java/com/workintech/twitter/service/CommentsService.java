package com.workintech.twitter.service;

import com.workintech.twitter.dto.patchrequest.CommentsPatchRequestDto;
import com.workintech.twitter.dto.request.CommentsRequestDto;
import com.workintech.twitter.dto.response.CommentsResponseDto;

import java.util.List;

public interface CommentsService {

    List<CommentsResponseDto> getAll();
    CommentsResponseDto findById(Long id);
    List<CommentsResponseDto> findByTweetId(Long tweetId);
    CommentsResponseDto create(CommentsRequestDto commentsRequestDto);
    CommentsResponseDto update(Long id, CommentsPatchRequestDto commentsPatchRequestDto, Long currentUserId);
    void deleteById(Long id, Long currentUserId, Long tweetOwnerId);
}
