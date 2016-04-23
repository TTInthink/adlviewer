package com.adl.service.impl;

import com.adl.service.DescriptionService;
import com.adl.domain.Description;
import com.adl.repository.DescriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Description.
 */
@Service
public class DescriptionServiceImpl implements DescriptionService{

    private final Logger log = LoggerFactory.getLogger(DescriptionServiceImpl.class);
    
    @Inject
    private DescriptionRepository descriptionRepository;
    
    /**
     * Save a description.
     * 
     * @param description the entity to save
     * @return the persisted entity
     */
    public Description save(Description description) {
        log.debug("Request to save Description : {}", description);
        Description result = descriptionRepository.save(description);
        return result;
    }

    /**
     *  Get all the descriptions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<Description> findAll(Pageable pageable) {
        log.debug("Request to get all Descriptions");
        Page<Description> result = descriptionRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one description by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Description findOne(String id) {
        log.debug("Request to get Description : {}", id);
        Description description = descriptionRepository.findOne(id);
        return description;
    }

    /**
     *  Delete the  description by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Description : {}", id);
        descriptionRepository.delete(id);
    }
}
