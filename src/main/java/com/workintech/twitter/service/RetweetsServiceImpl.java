package com.workintech.twitter.service;

import com.workintech.twitter.dto.request.RetweetsRequestDto;
import com.workintech.twitter.dto.response.RetweetsResponseDto;
import com.workintech.twitter.entity.Retweets;
import com.workintech.twitter.entity.Tweets;
import com.workintech.twitter.entity.Users;
import com.workintech.twitter.exception.RetweetsNotFoundException;
import com.workintech.twitter.exception.TweetsNotFoundException;
import com.workintech.twitter.exception.UsersNotFoundException;
import com.workintech.twitter.mapper.RetweetsMapper;
import com.workintech.twitter.repository.RetweetsRepository;
import com.workintech.twitter.repository.TweetsRepository;
import com.workintech.twitter.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RetweetsServiceImpl implements RetweetsService {
    @Autowired
    private final RetweetsRepository retweetsRepository;

    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private final TweetsRepository tweetsRepository;

    @Autowired
    private final RetweetsMapper retweetsMapper;

    @Override
    public List<RetweetsResponseDto> getAll() {
        return retweetsRepository
                .findAll()
                .stream()
                .map(retweetsMapper::toResponseDto)
                .toList();
    }

    @Override
    public RetweetsResponseDto findById(Long id) {
        Retweets retweet = retweetsRepository
                .findById(id)
                .orElseThrow(() -> new RetweetsNotFoundException(id + " id'li retweet bulunamadı."));
        return retweetsMapper.toResponseDto(retweet);
    }

    @Override
    public List<RetweetsResponseDto> findByUserId(Long userId) {
        Users user = usersRepository
                .findById(userId)
                .orElseThrow(() -> new UsersNotFoundException(userId + " id'li kullanıcı bulunamadı."));

        return retweetsRepository
                .findByUser(user)
                .stream()
                .map(retweetsMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<RetweetsResponseDto> findByOriginalTweetId(Long tweetId) {
        Tweets tweet = tweetsRepository
                .findById(tweetId)
                .orElseThrow(() -> new TweetsNotFoundException(tweetId + " id'li tweet bulunamadı."));

        return retweetsRepository
                .findByOriginalTweet(tweet)
                .stream()
                .map(retweetsMapper::toResponseDto)
                .toList();
    }

    @Override
    public RetweetsResponseDto create(RetweetsRequestDto retweetsRequestDto) {
        Users user = usersRepository
                .findById(retweetsRequestDto.userId())
                .orElseThrow(() -> new UsersNotFoundException(retweetsRequestDto.userId() + " id'li kullanıcı bulunamadı."));

        Tweets originalTweet = tweetsRepository
                .findById(retweetsRequestDto.originalTweetId())
                .orElseThrow(() -> new TweetsNotFoundException(retweetsRequestDto.originalTweetId() + " id'li tweet bulunamadı."));

        // mevcutta varmı
        Optional<Retweets> existingRetweet = retweetsRepository.findByUserAndOriginalTweet(user, originalTweet);
        if (existingRetweet.isPresent()) {
            throw new RuntimeException("Bu tweet zaten retweet edilmiş.");
        }

        if (originalTweet.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Kendi tweet'inizi retweet edemezsiniz.");
        }

        Retweets retweet = retweetsMapper.toEntity(retweetsRequestDto, user, originalTweet);
        retweet = retweetsRepository.save(retweet);

        originalTweet.addRetweet(retweet);

        return retweetsMapper.toResponseDto(retweet);
    }

    @Override
    public void deleteById(Long id, Long currentUserId) {
        Retweets retweet = retweetsRepository
                .findById(id)
                .orElseThrow(() -> new RetweetsNotFoundException(id + " id'li retweet bulunamadı."));

        if (!retweet.getUser().getId().equals(currentUserId)) {
            throw new RetweetsNotFoundException("Bu retweet'i sadece sahibi silebilir.");
        }

        retweet.getOriginalTweet().removeRetweet(retweet);
        retweetsRepository.deleteById(id);

    }
}
