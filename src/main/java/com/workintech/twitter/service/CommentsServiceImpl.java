package com.workintech.twitter.service;

import com.workintech.twitter.dto.patchrequest.CommentsPatchRequestDto;
import com.workintech.twitter.dto.request.CommentsRequestDto;
import com.workintech.twitter.dto.response.CommentsResponseDto;
import com.workintech.twitter.entity.Comments;
import com.workintech.twitter.entity.Tweets;
import com.workintech.twitter.entity.Users;
import com.workintech.twitter.exception.CommentsNotFoundException;
import com.workintech.twitter.exception.TweetsNotFoundException;
import com.workintech.twitter.exception.UsersNotFoundException;
import com.workintech.twitter.mapper.CommentsMapper;
import com.workintech.twitter.repository.CommentsRepository;
import com.workintech.twitter.repository.TweetsRepository;
import com.workintech.twitter.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl  implements CommentsService{

    @Autowired
    private final CommentsRepository commentsRepository;
    @Autowired
    private final UsersRepository usersRepository;
    @Autowired
    private final TweetsRepository tweetsRepository;
    @Autowired
    private final CommentsMapper commentsMapper;


    @Override
    public List<CommentsResponseDto> getAll() {
        return commentsRepository
                .findAll()
                .stream()
                .map(commentsMapper::toResponseDto)
                .toList();
    }

    @Override
    public CommentsResponseDto findById(Long id) {
        Comments comment = commentsRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException(id + " id'li yorum bulunamadı."));
        return commentsMapper.toResponseDto(comment);
    }

    @Override
    public List<CommentsResponseDto> findByTweetId(Long tweetId) {
        Tweets tweet = tweetsRepository
                .findById(tweetId)
                .orElseThrow(() -> new RuntimeException(tweetId + " id'li tweet bulunamadı."));

        return commentsRepository
                .findByTweet(tweet)
                .stream()
                .map(commentsMapper::toResponseDto)
                .toList();
    }

    @Override
    public CommentsResponseDto create(CommentsRequestDto commentsRequestDto) {
        Users user = usersRepository
                .findById(commentsRequestDto.userId())
                .orElseThrow(() -> new UsersNotFoundException(commentsRequestDto.userId() + " id'li kullanıcı bulunamadı."));

        Tweets tweet = tweetsRepository
                .findById(commentsRequestDto.tweetId())
                .orElseThrow(() -> new TweetsNotFoundException(commentsRequestDto.tweetId() + " id'li tweet bulunamadı."));

        Comments comment = commentsMapper.toEntity(commentsRequestDto, user, tweet);
        comment = commentsRepository.save(comment);
        user.addComment(comment);
        tweet.addComment(comment);
        return commentsMapper.toResponseDto(comment);
    }

    @Override
    public CommentsResponseDto replaceOrCreate(Long id, CommentsRequestDto commentsRequestDto) {
        Optional<Comments> optionalComment = commentsRepository.findById(id);

        Users user = usersRepository
                .findById(commentsRequestDto.userId())
                .orElseThrow(() -> new UsersNotFoundException(commentsRequestDto.userId() + " id'li kullanıcı bulunamadı."));

        Tweets tweet = tweetsRepository
                .findById(commentsRequestDto.tweetId())
                .orElseThrow(() -> new TweetsNotFoundException(commentsRequestDto.tweetId() + " id'li tweet bulunamadı."));

        if (optionalComment.isPresent()) {
            Comments existingComment = optionalComment.get();
            existingComment.setCommentContent(commentsRequestDto.commentContent());
            existingComment.setTweet(tweet);
            existingComment.setUser(user);
            commentsRepository.save(existingComment);
            return commentsMapper.toResponseDto(existingComment);
        } else {
            Comments newComment = commentsMapper.toEntity(commentsRequestDto, user, tweet);
            commentsRepository.save(newComment);
            return commentsMapper.toResponseDto(newComment);
        }
    }

    @Override
    public CommentsResponseDto update(Long id, CommentsPatchRequestDto commentsPatchRequestDto, Long currentUserId) {
        Comments commentToUpdate = commentsRepository
                .findById(id)
                .orElseThrow(() -> new CommentsNotFoundException(id + " id'li yorum bulunamadı."));

        if (!commentToUpdate.getUser().getId().equals(currentUserId)) {
            throw new CommentsNotFoundException("Bu yorumu sadece sahibi güncelleyebilir.");
        }

        commentToUpdate = commentsMapper.updateEntity(commentToUpdate, commentsPatchRequestDto);
        commentsRepository.save(commentToUpdate);

        return commentsMapper.toResponseDto(commentToUpdate);
    }

    @Override
    public void deleteById(Long id, Long currentUserId, Long tweetOwnerId) {
        Comments comment = commentsRepository
                .findById(id)
                .orElseThrow(() -> new CommentsNotFoundException(id + " id'li yorum bulunamadı."));

        Long commentOwnerId = comment.getUser().getId();

        if (!commentOwnerId.equals(currentUserId) && !tweetOwnerId.equals(currentUserId)) {
            throw new RuntimeException("Bu yorumu sadece yorum sahibi veya tweet sahibi silebilir.");
        }

        comment.getUser().removeComment(comment);
        comment.getTweet().removeComment(comment);
        commentsRepository.deleteById(id);

    }
}
