package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Likes;
import com.workintech.twitter.entity.Tweets;
import com.workintech.twitter.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes,Long> {
    Optional<Likes> findByUserAndTweet(Users user, Tweets tweet);
    List<Likes> findByTweet(Tweets tweet);
}
