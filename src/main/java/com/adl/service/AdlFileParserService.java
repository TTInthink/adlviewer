package com.adl.service;

import com.adl.domain.AdlFileParser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing AdlFileParser.
 */
public interface AdlFileParserService {

    /**
     * Save a adlFileParser.
     * 
     * @param adlFileParser the entity to save
     * @return the persisted entity
     */
    AdlFileParser save(AdlFileParser adlFileParser);

    /**
     *  Get all the adlFileParsers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AdlFileParser> findAll(Pageable pageable);

    /**
     *  Get the "id" adlFileParser.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    AdlFileParser findOne(String id);

    /**
     *  Delete the "id" adlFileParser.
     *  
     *  @param id the id of the entity
     */
    void delete(String id);
    
    void loadAdl(String adlFile,String id);
}
