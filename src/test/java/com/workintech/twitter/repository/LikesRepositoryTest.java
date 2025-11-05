package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Likes;
import com.workintech.twitter.entity.Tweets;
import com.workintech.twitter.entity.Users;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Likes Repository Testleri")
class LikesRepositoryTest {

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TweetsRepository tweetsRepository;

    private Users user;
    private Tweets tweet;
    private Likes like;

    @BeforeEach
    void init() {
        likesRepository.deleteAll();
        tweetsRepository.deleteAll();
        usersRepository.deleteAll();

        user = new Users();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFullName("Test User");
        user.setBio("Test bio");
        usersRepository.save(user);

        tweet = new Tweets();
        tweet.setContents("Test tweet");
        tweet.setUser(user);
        tweetsRepository.save(tweet);

        like = new Likes();
        like.setUser(user);
        like.setTweet(tweet);
        likesRepository.save(like);
    }

    @AfterEach
    void cleanup() {
        likesRepository.deleteAll();
        tweetsRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Test
    @DisplayName("Kullanıcı ve tweet ile like bul - varsa döndür")
    void findByUserAndTweet_exists() {
        Optional<Likes> found = likesRepository.findByUserAndTweet(user, tweet);

        assertThat(found).isPresent();
        assertThat(found.get().getUser().getUsername()).isEqualTo("testuser");
        assertThat(found.get().getTweet().getContents()).isEqualTo("Test tweet");
    }

    @Test
    @DisplayName("Kullanıcı ve tweet ile like bul - yoksa boş döndür")
    void findByUserAndTweet_notExists() {
        Users otherUser = new Users();
        otherUser.setUsername("otheruser");
        otherUser.setEmail("other@example.com");
        otherUser.setPassword("password123");
        otherUser.setFullName("Other User");
        otherUser.setBio("Other bio");
        usersRepository.save(otherUser);

        Optional<Likes> found = likesRepository.findByUserAndTweet(otherUser, tweet);

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Tweet'e göre like'ları bul - varsa döndür")
    void findByTweet_exists() {
        List<Likes> found = likesRepository.findByTweet(tweet);

        assertThat(found).isNotEmpty();
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTweet().getContents()).isEqualTo("Test tweet");
    }

    @Test
    @DisplayName("Tweet'e göre like'ları bul - yoksa boş liste döndür")
    void findByTweet_notExists() {
        Tweets otherTweet = new Tweets();
        otherTweet.setContents("Other tweet");
        otherTweet.setUser(user);
        tweetsRepository.save(otherTweet);

        List<Likes> found = likesRepository.findByTweet(otherTweet);

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Birden fazla like varsa hepsini getir")
    void findByTweet_multipleLikes() {
        Users user2 = new Users();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("password123");
        user2.setFullName("User Two");
        user2.setBio("Bio 2");
        usersRepository.save(user2);

        Likes like2 = new Likes();
        like2.setUser(user2);
        like2.setTweet(tweet);
        likesRepository.save(like2);

        List<Likes> found = likesRepository.findByTweet(tweet);

        assertThat(found).hasSize(2);
    }

    @Test
    @DisplayName("Yeni like kaydet")
    void saveLike() {
        Users newUser = new Users();
        newUser.setUsername("newuser");
        newUser.setEmail("new@example.com");
        newUser.setPassword("password123");
        newUser.setFullName("New User");
        newUser.setBio("New bio");
        usersRepository.save(newUser);

        Likes newLike = new Likes();
        newLike.setUser(newUser);
        newLike.setTweet(tweet);

        Likes saved = likesRepository.save(newLike);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUser().getUsername()).isEqualTo("newuser");

        Optional<Likes> fromDb = likesRepository.findById(saved.getId());
        assertThat(fromDb).isPresent();
    }

    @Test
    @DisplayName("Like sil")
    void deleteLike() {
        likesRepository.delete(like);

        Optional<Likes> found = likesRepository.findByUserAndTweet(user, tweet);
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Like'ın timestamp'i otomatik oluşturulmalı")
    void likeShouldHaveTimestamp() {
        assertThat(like.getLikedAt()).isNotNull();
    }
}