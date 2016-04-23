package com.adl.service.impl;

import com.adl.service.AdlparserService;
import com.adl.domain.Adlparser;
import com.adl.repository.AdlparserRepository;

import org.apache.commons.io.IOUtils;
import org.openehr.am.archetype.Archetype;
import org.openehr.rm.common.resource.ResourceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import se.acode.openehr.parser.ADLParser;

import javax.inject.Inject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Service Implementation for managing Adlparser.
 */
@Service
public class AdlparserServiceImpl implements AdlparserService{

    private final Logger log = LoggerFactory.getLogger(AdlparserServiceImpl.class);
    
    @Inject
    private AdlparserRepository adlparserRepository;
    
    /**
     * Save a adlparser.
     * 
     * @param adlparser the entity to save
     * @return the persisted entity
     */
    public Adlparser save(Adlparser adlparser) {
        log.debug("Request to save Adlparser : {}", adlparser);
        Adlparser result = adlparserRepository.save(adlparser);
        return result;
    }

    /**
     *  Get all the adlparsers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<Adlparser> findAll(Pageable pageable) {
        log.debug("Request to get all Adlparsers");
        Page<Adlparser> result = adlparserRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one adlparser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Adlparser findOne(String id) {
        log.debug("Request to get Adlparser : {}", id);
        Adlparser adlparser = adlparserRepository.findOne(id);
//        String content=adlparser.getContent();
//        try {
//			InputStream in = IOUtils.toInputStream(content, "UTF-8");
//			ADLParser parser = new ADLParser(in);
//			Archetype archetype = parser.parse();
////			ResourceDescription description = archetype.getDescription();
////			Map<String, String> originalAuthor = description.getOriginalAuthor();
////			log.debug("Request to get Adlparser Original author: {}", originalAuthor.get("name"));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        return adlparser;
    }

    /**
     *  Delete the  adlparser by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Adlparser : {}", id);
        adlparserRepository.delete(id);
    }
}
