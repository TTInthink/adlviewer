package com.adl.repository;

import com.adl.domain.AdlFileParser;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the AdlFileParser entity.
 */
public interface AdlFileParserRepository extends MongoRepository<AdlFileParser,String> {

}
