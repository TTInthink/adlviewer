package com.adl.repository;

import com.adl.domain.Description;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Description entity.
 */
public interface DescriptionRepository extends MongoRepository<Description,String> {

}
