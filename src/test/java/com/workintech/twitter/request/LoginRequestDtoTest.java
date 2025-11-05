package com.workintech.twitter.request;
import com.workintech.twitter.dto.request.LoginRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Login Request DTO Testleri")
class LoginRequestDtoTest {

    @Test
    @DisplayName("DTO başarıyla oluşturulmalı")
    void shouldCreateDto() {
        LoginRequestDto dto = new LoginRequestDto("yagmur@test.com", "password123");

        assertEquals("yagmur@test.com", dto.usernameOrEmail());
        assertEquals("password123", dto.password());
    }

    @Test
    @DisplayName("Username ile oluşturulabilmeli")
    void shouldCreateWithUsername() {
        LoginRequestDto dto = new LoginRequestDto("yagmur", "password123");

        assertEquals("yagmur", dto.usernameOrEmail());
    }

    @Test
    @DisplayName("Email ile oluşturulabilmeli")
    void shouldCreateWithEmail() {
        LoginRequestDto dto = new LoginRequestDto("yagmur@test.com", "password123");

        assertEquals("yagmur@test.com", dto.usernameOrEmail());
    }

    @Test
    @DisplayName("Aynı değerlere sahip DTO'lar eşit olmalı")
    void shouldBeEqual() {
        LoginRequestDto dto1 = new LoginRequestDto("yagmur", "password123");
        LoginRequestDto dto2 = new LoginRequestDto("yagmur", "password123");

        assertEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Farklı değerlere sahip DTO'lar eşit olmamalı")
    void shouldNotBeEqual() {
        LoginRequestDto dto1 = new LoginRequestDto("yagmur", "password123");
        LoginRequestDto dto2 = new LoginRequestDto("ahmet", "password123");

        assertNotEquals(dto1, dto2);
    }
}