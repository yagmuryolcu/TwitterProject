package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Tweets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetsRepository extends JpaRepository<Tweets,Long> {
}
