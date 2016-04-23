package com.adl.repository;

import com.adl.domain.AdlFile;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the AdlFile entity.
 */
public interface AdlFileRepository extends MongoRepository<AdlFile,String> {

}
