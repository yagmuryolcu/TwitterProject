package com.workintech.twitter.service;

import com.workintech.twitter.dto.patchrequest.LikesPatchRequestDto;
import com.workintech.twitter.dto.request.LikesRequestDto;
import com.workintech.twitter.dto.response.LikesResponseDto;
import com.workintech.twitter.entity.Likes;
import com.workintech.twitter.entity.Tweets;
import com.workintech.twitter.entity.Users;
import com.workintech.twitter.exception.LikesNotFoundException;
import com.workintech.twitter.exception.TweetsNotFoundException;
import com.workintech.twitter.mapper.LikesMapper;
import com.workintech.twitter.repository.LikesRepository;
import com.workintech.twitter.repository.TweetsRepository;
import com.workintech.twitter.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {

    @Autowired
    private final LikesRepository likesRepository;

    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private final TweetsRepository tweetsRepository;

    @Autowired
    private final  LikesMapper likesMapper;


    @Override
    public List<LikesResponseDto> getAll() {
        return likesRepository
                .findAll()
                .stream()
                .map(likesMapper::toResponseDto)
                .toList();
    }

    @Override
    public LikesResponseDto findById(Long id) {
        Optional<Likes> optionalLikes =likesRepository.findById(id);

        if (optionalLikes.isPresent())
        return likesMapper.toResponseDto(optionalLikes.get());

        throw new LikesNotFoundException(id + "id'li beğeni bulunamadı.");
        }

    @Override
    public List<LikesResponseDto> findByTweetId(Long tweetId) {
        Tweets tweet = tweetsRepository
                .findById(tweetId)
                .orElseThrow(() -> new TweetsNotFoundException(tweetId + " id'li tweet bulunamadı."));

        return likesRepository
                .findByTweet(tweet)
                .stream()
                .map(likesMapper::toResponseDto)
                .toList();
    }

    @Override
    public LikesResponseDto replaceOrCreate(Long id, LikesRequestDto likesRequestDto) {
        Optional<Likes> optionalLikes = likesRepository.findById(id);

        Users user = usersRepository
                .findById(likesRequestDto.userId())
                .orElseThrow(() -> new RuntimeException(likesRequestDto.userId() + " id'li kullanıcı bulunamadı."));

        Tweets tweet = tweetsRepository
                .findById(likesRequestDto.tweetId())
                .orElseThrow(() -> new RuntimeException(likesRequestDto.tweetId() + " id'li tweet bulunamadı."));


        Likes likesToReplaceOrCreate = likesMapper.toEntity(likesRequestDto,user,tweet);

        if (optionalLikes.isPresent()) {
            likesToReplaceOrCreate.setId(id);
            return likesMapper.toResponseDto(likesRepository.save(likesToReplaceOrCreate));
        }

        return likesMapper.toResponseDto(likesRepository.save(likesToReplaceOrCreate));
    }


    @Override
    public LikesResponseDto create(LikesRequestDto likesRequestDto) {
        Users user = usersRepository
                .findById(likesRequestDto.userId())
                .orElseThrow(() -> new RuntimeException(likesRequestDto.userId() + " id'li kullanıcı bulunamadı."));

        Tweets tweet = tweetsRepository
                .findById(likesRequestDto.tweetId())
                .orElseThrow(() -> new RuntimeException(likesRequestDto.tweetId() + " id'li tweet bulunamadı."));

        Optional<Likes> existingLike = likesRepository.findByUserAndTweet(user, tweet);
        if (existingLike.isPresent()) {
            throw new RuntimeException("Bu tweet zaten beğenilmiş.");
        }

        Likes like = likesMapper.toEntity(likesRequestDto, user, tweet);
        like = likesRepository.save(like);

        tweet.addLike(like);

        return likesMapper.toResponseDto(like);
    }


    @Override
    public LikesResponseDto update(Long id, LikesPatchRequestDto likesPatchRequestDto) {
        Likes likesToUpdate =likesRepository
                .findById(id)
                .orElseThrow(()-> new LikesNotFoundException(id + "id'li beğeni bulunamadi"));
        likesMapper.updateEntity(likesToUpdate,likesPatchRequestDto);
        return likesMapper.toResponseDto(likesRepository.save(likesToUpdate));
    }

    @Override
    public void deleteByUserAndTweet(Long userId, Long tweetId) {
        Users user = usersRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException(userId + " id'li kullanıcı bulunamadı."));

        Tweets tweet = tweetsRepository
                .findById(tweetId)
                .orElseThrow(() -> new RuntimeException(tweetId + " id'li tweet bulunamadı."));

        Likes like = likesRepository
                .findByUserAndTweet(user, tweet)
                .orElseThrow(() -> new RuntimeException("Bu tweet için beğeni bulunamadı."));

        tweet.removeLike(like);
        likesRepository.delete(like);
    }

}
