package com.adl.repository;

import com.adl.domain.Author;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Author entity.
 */
public interface AuthorRepository extends MongoRepository<Author,String> {

}
