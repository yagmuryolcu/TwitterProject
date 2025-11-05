package com.workintech.twitter.response;

import com.workintech.twitter.dto.patchrequest.LikesPatchRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LikesPatchRequestDtoTest {

    @Test
    @DisplayName("DTO başarıyla oluşturulmalı")
    void shouldCreateDto() {
        Long userId = 1L;
        Long tweetId = 100L;
        String username = "Yağmur";

        LikesPatchRequestDto dto = new LikesPatchRequestDto(userId, tweetId, username);

        assertEquals(1L, dto.userId());
        assertEquals(100L, dto.tweetId());
        assertEquals("Yağmur", dto.username());
    }

    @Test
    @DisplayName("Null değerlerle çalışmalı")
    void shouldWorkWithNullValues() {
        LikesPatchRequestDto dto = new LikesPatchRequestDto(null, null, null);

        assertNull(dto.userId());
        assertNull(dto.tweetId());
        assertNull(dto.username());
    }

    @Test
    @DisplayName("Aynı değerlere sahip DTO'lar eşit olmalı")
    void shouldBeEqual() {
        LikesPatchRequestDto dto1 = new LikesPatchRequestDto(1L, 100L, "Yağmur");
        LikesPatchRequestDto dto2 = new LikesPatchRequestDto(1L, 100L, "Yağmur");

        assertEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqual() {
        LikesPatchRequestDto dto1 = new LikesPatchRequestDto(1L, 100L, "Yağmur");
        LikesPatchRequestDto dto2 = new LikesPatchRequestDto(2L, 100L, "Yağmur");

        assertNotEquals(dto1, dto2);
    }
}