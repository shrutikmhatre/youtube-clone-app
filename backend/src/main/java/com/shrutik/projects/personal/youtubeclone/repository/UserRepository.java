package com.shrutik.projects.personal.youtubeclone.repository;

import com.shrutik.projects.personal.youtubeclone.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findBySub(String sub);
}
