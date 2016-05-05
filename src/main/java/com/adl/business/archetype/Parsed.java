package com.adl.business.archetype;

import org.openehr.am.archetype.Archetype;

public abstract class Parsed<T> {

	private Archetype archetype;
	
	public Parsed(Archetype archetype){
		this.archetype=archetype;
	}
	
	public abstract T parseArchetype();
}
