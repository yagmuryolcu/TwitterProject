package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Retweets;
import com.workintech.twitter.entity.Tweets;
import com.workintech.twitter.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RetweetsRepository extends JpaRepository <Retweets,Long> {

    List<Retweets> findByUser(Users user);
    List<Retweets> findByOriginalTweet(Tweets originalTweet);
    Optional<Retweets> findByUserAndOriginalTweet(Users user, Tweets originalTweet);
}
