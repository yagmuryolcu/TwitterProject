package com.workintech.twitter.mapper;

import com.workintech.twitter.dto.patchrequest.CommentsPatchRequestDto;
import com.workintech.twitter.dto.request.CommentsRequestDto;
import com.workintech.twitter.dto.response.CommentsResponseDto;
import com.workintech.twitter.entity.Comments;
import com.workintech.twitter.entity.Tweets;
import com.workintech.twitter.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class CommentsMapper {

    public Comments toEntity(CommentsRequestDto commentsRequestDto, Users user, Tweets tweet) {
        Comments comment = new Comments();
        comment.setCommentContent(commentsRequestDto.commentContent());
        comment.setUser(user);
        comment.setTweet(tweet);
        return comment;
    }

    public CommentsResponseDto toResponseDto(Comments comment) {
        return new CommentsResponseDto(
                comment.getId(),
                comment.getCommentContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getTweet().getTweetId()
        );
    }

    public Comments updateEntity(Comments commentToUpdate, CommentsPatchRequestDto commentsPatchRequestDto) {
        if (commentsPatchRequestDto.commentContent() != null) {
            commentToUpdate.setCommentContent(commentsPatchRequestDto.commentContent());
        }
        return commentToUpdate;
    }

}
