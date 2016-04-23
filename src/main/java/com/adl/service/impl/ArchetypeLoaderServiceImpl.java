package com.adl.service.impl;

import java.io.InputStream;
import java.util.concurrent.Future;

import org.openehr.am.archetype.Archetype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import se.acode.openehr.parser.ADLParser;

import com.adl.service.ArchetypeLoaderService;

@Service("archetypeLoaderService")
public class ArchetypeLoaderServiceImpl implements ArchetypeLoaderService{

	private final Logger log = LoggerFactory.getLogger(ArchetypeLoaderServiceImpl.class);
    
	private Archetype archetype;

	@Autowired
	private ResourceLoader resourceLoader;
	
	@Override
	@Async
	public Future<Archetype> loadAdl(String adlFile) {
		try {
			log.debug("-----------------------LOAD ADL-----------");
			ADLParser parser = new ADLParser(loadFromClasspath(adlFile));
			this.archetype = parser.parse();
			return new AsyncResult<Archetype>(this.archetype);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected InputStream loadFromClasspath(String adl) throws Exception {
		Resource resource = this.resourceLoader.getResource("upload/" + adl+".adl");
		return resource.getInputStream();
	}


}
