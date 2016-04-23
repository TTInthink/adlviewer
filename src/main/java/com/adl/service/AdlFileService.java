package com.adl.service;

import com.adl.domain.AdlFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing AdlFile.
 */
public interface AdlFileService {

    /**
     * Save a adlFile.
     * 
     * @param adlFile the entity to save
     * @return the persisted entity
     */
    AdlFile save(AdlFile adlFile);

    /**
     *  Get all the adlFiles.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AdlFile> findAll(Pageable pageable);

    /**
     *  Get the "id" adlFile.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    AdlFile findOne(String id);

    /**
     *  Delete the "id" adlFile.
     *  
     *  @param id the id of the entity
     */
    void delete(String id);
}
