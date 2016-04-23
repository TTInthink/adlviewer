package com.adl.service;

import com.adl.domain.Adlparser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.InputStream;
import java.util.List;

/**
 * Service Interface for managing Adlparser.
 */
public interface AdlparserService {

    /**
     * Save a adlparser.
     * 
     * @param adlparser the entity to save
     * @return the persisted entity
     */
    Adlparser save(Adlparser adlparser);

    /**
     *  Get all the adlparsers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Adlparser> findAll(Pageable pageable);

    /**
     *  Get the "id" adlparser.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Adlparser findOne(String id);

    /**
     *  Delete the "id" adlparser.
     *  
     *  @param id the id of the entity
     */
    void delete(String id);

}
