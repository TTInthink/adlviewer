package com.adl.service;

import com.adl.domain.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Description.
 */
public interface DescriptionService {

    /**
     * Save a description.
     * 
     * @param description the entity to save
     * @return the persisted entity
     */
    Description save(Description description);

    /**
     *  Get all the descriptions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Description> findAll(Pageable pageable);

    /**
     *  Get the "id" description.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Description findOne(String id);

    /**
     *  Delete the "id" description.
     *  
     *  @param id the id of the entity
     */
    void delete(String id);
}
