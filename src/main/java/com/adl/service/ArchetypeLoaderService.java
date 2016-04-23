package com.adl.service;

import java.util.concurrent.Future;

import org.openehr.am.archetype.Archetype;

public interface ArchetypeLoaderService {

	Future<Archetype> loadAdl(String adlFile);
}
