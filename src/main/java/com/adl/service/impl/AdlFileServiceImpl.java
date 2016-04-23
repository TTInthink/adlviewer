package com.adl.service.impl;

import com.adl.service.AdlFileService;
import com.adl.domain.AdlFile;
import com.adl.repository.AdlFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing AdlFile.
 */
@Service
public class AdlFileServiceImpl implements AdlFileService{

    private final Logger log = LoggerFactory.getLogger(AdlFileServiceImpl.class);
    
    @Inject
    private AdlFileRepository adlFileRepository;
    
    /**
     * Save a adlFile.
     * 
     * @param adlFile the entity to save
     * @return the persisted entity
     */
    public AdlFile save(AdlFile adlFile) {
        log.debug("Request to save AdlFile : {}", adlFile);
        AdlFile result = adlFileRepository.save(adlFile);
        return result;
    }

    /**
     *  Get all the adlFiles.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<AdlFile> findAll(Pageable pageable) {
        log.debug("Request to get all AdlFiles");
        Page<AdlFile> result = adlFileRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one adlFile by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public AdlFile findOne(String id) {
        log.debug("Request to get AdlFile : {}", id);
        AdlFile adlFile = adlFileRepository.findOne(id);
        return adlFile;
    }

    /**
     *  Delete the  adlFile by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete AdlFile : {}", id);
        adlFileRepository.delete(id);
    }
}
