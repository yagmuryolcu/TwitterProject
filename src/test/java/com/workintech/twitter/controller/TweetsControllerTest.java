package com.workintech.twitter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workintech.twitter.dto.patchrequest.TweetsPatchRequestDto;
import com.workintech.twitter.dto.request.TweetsRequestDto;
import com.workintech.twitter.dto.response.TweetsResponseDto;
import com.workintech.twitter.service.TweetsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TweetsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TweetsService tweetService;

    @InjectMocks
    private TweetsController tweetsController;

    private TweetsRequestDto tweetRequestDto;
    private TweetsResponseDto tweetResponseDto;
    private TweetsPatchRequestDto tweetsPatchRequestDto;
    private final Long TEST_TWEET_ID = 1L;
    private final Long TEST_USER_ID = 1L;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tweetsController).build();

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
    void getAll() throws Exception {
        List<TweetsResponseDto> tweets = Arrays.asList(tweetResponseDto);
        when(tweetService.getAll()).thenReturn(tweets);

        mockMvc.perform(get("/tweets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tweetId").value(TEST_TWEET_ID))
                .andExpect(jsonPath("$[0].contents").value("Test tweet content"))
                .andExpect(jsonPath("$[0].username").value("testuser"));

        verify(tweetService, times(1)).getAll();
    }

    @Test
    void findById() throws Exception {
        when(tweetService.findById(TEST_TWEET_ID)).thenReturn(tweetResponseDto);
        mockMvc.perform(get("/tweets/{id}", TEST_TWEET_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tweetId").value(TEST_TWEET_ID))
                .andExpect(jsonPath("$.contents").value("Test tweet content"))
                .andExpect(jsonPath("$.userId").value(TEST_USER_ID))
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(tweetService, times(1)).findById(TEST_TWEET_ID);
    }

    @Test
    void create() throws Exception {
        when(tweetService.create(any(TweetsRequestDto.class))).thenReturn(tweetResponseDto);

        mockMvc.perform(post("/tweets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(tweetRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tweetId").value(TEST_TWEET_ID))
                .andExpect(jsonPath("$.contents").value("Test tweet content"))
                .andExpect(jsonPath("$.userId").value(TEST_USER_ID));

        verify(tweetService, times(1)).create(any(TweetsRequestDto.class));
    }

    @Test
    void replaceOrCreate() throws Exception {
        when(tweetService.replaceOrCreate(eq(TEST_TWEET_ID), any(TweetsRequestDto.class)))
                .thenReturn(tweetResponseDto);

        mockMvc.perform(put("/tweets/{id}", TEST_TWEET_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(tweetRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tweetId").value(TEST_TWEET_ID))
                .andExpect(jsonPath("$.contents").value("Test tweet content"));

        verify(tweetService, times(1)).replaceOrCreate(eq(TEST_TWEET_ID), any(TweetsRequestDto.class));
    }

    @Test
    void update() throws Exception {

        TweetsResponseDto updatedResponse = new TweetsResponseDto(
                TEST_TWEET_ID,
                "Updated tweet content",
                LocalDateTime.now(),
                TEST_USER_ID,
                "testuser"
        );
        when(tweetService.update(eq(TEST_TWEET_ID), any(TweetsPatchRequestDto.class)))
                .thenReturn(updatedResponse);
        mockMvc.perform(patch("/tweets/{id}", TEST_TWEET_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(tweetsPatchRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tweetId").value(TEST_TWEET_ID))
                .andExpect(jsonPath("$.contents").value("Updated tweet content"));

        verify(tweetService, times(1)).update(eq(TEST_TWEET_ID), any(TweetsPatchRequestDto.class));
    }

    @Test
    void deleteById() throws Exception {
        doNothing().when(tweetService).deleteById(TEST_TWEET_ID);

        mockMvc.perform(delete("/tweets/{id}", TEST_TWEET_ID))
                .andExpect(status().isNoContent());

        verify(tweetService, times(1)).deleteById(TEST_TWEET_ID);
    }
}