package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users,Long> {
}
