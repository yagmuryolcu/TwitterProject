package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments,Long> {
}
