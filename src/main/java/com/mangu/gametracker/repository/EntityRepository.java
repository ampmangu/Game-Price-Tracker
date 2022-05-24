package com.mangu.gametracker.repository;

import com.mangu.gametracker.model.Entity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableMongoRepositories(basePackages = "com.mangu.gametracker.repository")
@Repository
public interface EntityRepository extends MongoRepository<Entity, String> {
    List<Entity> findByUrl(String url);
}
