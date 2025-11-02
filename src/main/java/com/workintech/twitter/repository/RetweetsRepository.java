package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Retweets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetweetsRepository extends JpaRepository <Retweets,Long> {
}
