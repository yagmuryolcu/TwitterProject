package com.workintech.twitter.service;

import com.workintech.twitter.dto.patchrequest.TweetsPatchRequestDto;
import com.workintech.twitter.dto.request.TweetsRequestDto;
import com.workintech.twitter.dto.response.TweetsResponseDto;
import com.workintech.twitter.entity.Tweets;
import com.workintech.twitter.entity.Users;
import com.workintech.twitter.exception.TweetsNotFoundException;
import com.workintech.twitter.mapper.TweetsMapper;
import com.workintech.twitter.repository.TweetsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TweetsServiceImplTest {

    @Mock
    private TweetsRepository tweetsRepository;

    @Mock
    private TweetsMapper tweetsMapper;

    @InjectMocks
    private TweetsServiceImpl tweetsService;

    private Tweets tweet;
    private TweetsRequestDto tweetRequestDto;
    private TweetsResponseDto tweetResponseDto;
    private TweetsPatchRequestDto tweetsPatchRequestDto;
    private Users user;
    private final Long TEST_TWEET_ID = 1L;
    private final Long TEST_USER_ID = 1L;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setId(TEST_USER_ID);
        user.setUsername("testuser");

        tweet = new Tweets();
        tweet.setTweetId(TEST_TWEET_ID);
        tweet.setContents("Test tweet content");
        tweet.setCreatedAt(LocalDateTime.now());
        tweet.setUser(user);

        tweetRequestDto = new TweetsRequestDto(
                "Test tweet content",
                TEST_USER_ID
        );

        tweetResponseDto = new TweetsResponseDto(
                TEST_TWEET_ID,
                "Test tweet content",
                LocalDateTime.now(),
                TEST_USER_ID,
                "testuser"
        );

        tweetsPatchRequestDto = new TweetsPatchRequestDto(
                "Updated tweet content"
        );
    }

    @Test
    void getAll() {
        List<Tweets> tweets = Arrays.asList(tweet);
        when(tweetsRepository.findAll()).thenReturn(tweets);
        when(tweetsMapper.toResponseDto(any(Tweets.class))).thenReturn(tweetResponseDto);

        List<TweetsResponseDto> result = tweetsService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(tweetResponseDto.tweetId(), result.get(0).tweetId());
        verify(tweetsRepository, times(1)).findAll();
        verify(tweetsMapper, times(1)).toResponseDto(any(Tweets.class));
    }

    @Test
    void findById() {
        when(tweetsRepository.findById(TEST_TWEET_ID)).thenReturn(Optional.of(tweet));
        when(tweetsMapper.toResponseDto(tweet)).thenReturn(tweetResponseDto);

        TweetsResponseDto result = tweetsService.findById(TEST_TWEET_ID);

        assertNotNull(result);
        assertEquals(TEST_TWEET_ID, result.tweetId());
        assertEquals("Test tweet content", result.contents());
        verify(tweetsRepository, times(1)).findById(TEST_TWEET_ID);
        verify(tweetsMapper, times(1)).toResponseDto(tweet);
    }

    @Test
    void create() {
        when(tweetsMapper.toEntity(tweetRequestDto)).thenReturn(tweet);
        when(tweetsRepository.save(tweet)).thenReturn(tweet);
        when(tweetsMapper.toResponseDto(tweet)).thenReturn(tweetResponseDto);

        TweetsResponseDto result = tweetsService.create(tweetRequestDto);

        assertNotNull(result);
        assertEquals(tweetResponseDto.tweetId(), result.tweetId());
        assertEquals(tweetResponseDto.contents(), result.contents());
        verify(tweetsMapper, times(1)).toEntity(tweetRequestDto);
        verify(tweetsRepository, times(1)).save(tweet);
        verify(tweetsMapper, times(1)).toResponseDto(tweet);
    }

    @Test
    void replaceOrCreate() {
        when(tweetsRepository.findById(TEST_TWEET_ID)).thenReturn(Optional.of(tweet));
        when(tweetsMapper.toEntity(tweetRequestDto)).thenReturn(tweet);
        when(tweetsRepository.save(any(Tweets.class))).thenReturn(tweet);
        when(tweetsMapper.toResponseDto(any(Tweets.class))).thenReturn(tweetResponseDto);

        TweetsResponseDto result = tweetsService.replaceOrCreate(TEST_TWEET_ID, tweetRequestDto);

        assertNotNull(result);
        assertEquals(tweetResponseDto.tweetId(), result.tweetId());
        verify(tweetsRepository, times(1)).findById(TEST_TWEET_ID);
        verify(tweetsRepository, times(1)).save(any(Tweets.class));
    }

    @Test
    void update() {
        when(tweetsRepository.findById(TEST_TWEET_ID)).thenReturn(Optional.of(tweet));
        when(tweetsRepository.save(tweet)).thenReturn(tweet);
        when(tweetsMapper.toResponseDto(tweet)).thenReturn(tweetResponseDto);

        TweetsResponseDto result = tweetsService.update(TEST_TWEET_ID, tweetsPatchRequestDto);
        assertNotNull(result);
        assertEquals(tweetResponseDto.tweetId(), result.tweetId());
        verify(tweetsRepository, times(1)).findById(TEST_TWEET_ID);
        verify(tweetsMapper, times(1)).updateEntity(tweet, tweetsPatchRequestDto);
        verify(tweetsRepository, times(1)).save(tweet);
    }
    @Test
    void deleteById() {

        doNothing().when(tweetsRepository).deleteById(TEST_TWEET_ID);
        tweetsService.deleteById(TEST_TWEET_ID);
        verify(tweetsRepository, times(1)).deleteById(TEST_TWEET_ID);
    }
}