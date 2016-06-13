package com.adl.business.archetype;

import org.openehr.am.archetype.Archetype;

import com.adl.domain.AdlFileParserIdentification;

public class ArchetypeIdentification<T extends AdlFileParserIdentification> extends Parsed<T> {

	private Archetype archetype;
	
	private T identification;
	
		
	public ArchetypeIdentification(Archetype archetype,T identification) {
		super(archetype);
		this.archetype=archetype;
		this.identification=identification;
	}
	
	

	@Override
	public T parseArchetype() {

		identification.setAdlVersion(this.archetype.getAdlVersion());
		identification.setuId(this.archetype.getUid()==null?"":this.archetype.getUid().toString());
		identification.setControlled(Boolean.toString(this.archetype.isControlled()));
		return identification;
	}

}
