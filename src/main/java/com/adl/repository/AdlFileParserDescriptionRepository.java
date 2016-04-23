package com.adl.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adl.domain.AdlFileParserDescription;

public interface AdlFileParserDescriptionRepository extends MongoRepository<AdlFileParserDescription,String>{

}
