package com.shrutik.projects.personal.youtubeclone.repository;

import com.shrutik.projects.personal.youtubeclone.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends MongoRepository<Video,String> {

}
