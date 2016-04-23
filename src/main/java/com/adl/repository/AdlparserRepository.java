package com.adl.repository;

import java.io.InputStream;

import com.adl.domain.Adlparser;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Adlparser entity.
 */
public interface AdlparserRepository extends MongoRepository<Adlparser,String> {

}
