package com.adl.service.impl;

import com.adl.service.AdlFileParserService;
import com.adl.service.ArchetypeLoaderService;
import com.adl.business.archetype.ArchetypeDescription;
import com.adl.business.archetype.ArchetypeIdentification;
import com.adl.domain.AdlFileParser;
import com.adl.domain.AdlFileParserDescription;
import com.adl.domain.AdlFileParserIdentification;
import com.adl.repository.AdlFileParserRepository;

import org.openehr.am.archetype.Archetype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.inject.Inject;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Service Implementation for managing AdlFileParser.
 */
@Service
public class AdlFileParserServiceImpl implements AdlFileParserService{

    private final Logger log = LoggerFactory.getLogger(AdlFileParserServiceImpl.class);
    
    @Inject
    private AdlFileParserRepository adlFileParserRepository;
    
    @Inject
    private ArchetypeLoaderService archetypeLoaderService;
    
//    @Inject
//    private AdlFileParserDescriptionRepository adlFileParserDescriptionRepository;
//    
//    @Inject
//    private AdlFileParserIdentificationRepository adlFileParserIdentificationRepository; 

    private Archetype archetype;
    
    /**
     * Save a adlFileParser.
     * 
     * @param adlFileParser the entity to save
     * @return the persisted entity
     */
    public AdlFileParser save(AdlFileParser adlFileParser) {
        log.debug("Request to save AdlFileParser : {}", adlFileParser);
        AdlFileParser result = adlFileParserRepository.save(adlFileParser);
        loadAdl(result);
        return result;
    }

    /**
     *  Get all the adlFileParsers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<AdlFileParser> findAll(Pageable pageable) {
        log.debug("Request to get all AdlFileParsers");
        Page<AdlFileParser> result = adlFileParserRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one adlFileParser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public AdlFileParser findOne(String id) {
        log.debug("Request to get AdlFileParser : {}", id);
        AdlFileParser adlFileParser = adlFileParserRepository.findOne(id);
        return adlFileParser;
    }

    /**
     *  Delete the  adlFileParser by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete AdlFileParser : {}", id);
        adlFileParserRepository.delete(id);
    }
    
    private void loadAdl(AdlFileParser adlFileParser){
    	Future<Archetype> archetypeFuture =archetypeLoaderService.loadAdl(adlFileParser.getArchetypeID());
		log.debug("Request to load ADL File :"+adlFileParser.getArchetypeID());
		try {
			//wait for archetype to finish, else go to sleep, and release the process to other thread
			while(archetypeFuture.isDone()){
				Thread.sleep(100); //100-millisecond pause between each check
			}
			this.archetype=archetypeFuture.get();
			parseArchetype(adlFileParser);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
    private void parseArchetype(AdlFileParser adlFileParser){
    	
		AdlFileParserDescription adlFileParserDescription=new AdlFileParserDescription();
		ArchetypeDescription<AdlFileParserDescription> archetypeDescription=new ArchetypeDescription<AdlFileParserDescription>(archetype,adlFileParserDescription);
		AdlFileParserDescription description=archetypeDescription.parseArchetype();
		adlFileParser.setAdlFileParserDescription(description);
		
		AdlFileParserIdentification adlFileParserIdentification=new AdlFileParserIdentification();
		ArchetypeIdentification<AdlFileParserIdentification> archetypeIdentification=new ArchetypeIdentification<AdlFileParserIdentification>(archetype,adlFileParserIdentification);
		AdlFileParserIdentification identification=archetypeIdentification.parseArchetype();
		adlFileParser.setAdlFileParserIdentification(identification);
		
		adlFileParserRepository.save(adlFileParser);
		
    }

	@Override
	public void loadAdl(String adlFile,String id) {
		// TODO Auto-generated method stub
		Future<Archetype> archetypeFuture =archetypeLoaderService.loadAdl(adlFile);
		log.debug("Request to load ADL File :"+adlFile);
		try {
			//wait for archetype to finish, else go to sleep, and release the process to other thread
			while(archetypeFuture.isDone()){
				Thread.sleep(100); //100-millisecond pause between each check
			}
			this.archetype=archetypeFuture.get();
//			parseArchetype(id);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

  
    
}
