package com.adl.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adl.domain.AdlFileParserIdentification;

public interface AdlFileParserIdentificationRepository extends MongoRepository<AdlFileParserIdentification,String>{

}
