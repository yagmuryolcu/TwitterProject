package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes,Long> {
}
