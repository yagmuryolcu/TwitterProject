package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Comments;
import com.workintech.twitter.entity.Tweets;
import com.workintech.twitter.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments,Long> {

    List<Comments> findByTweet(Tweets tweet);
    List<Comments> findByUser(Users user);
    List<Comments> findByTweetOrderByCreatedAtDesc(Tweets tweet);
}
