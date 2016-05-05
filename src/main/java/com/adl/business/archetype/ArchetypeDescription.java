package com.adl.business.archetype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openehr.am.archetype.Archetype;
import org.openehr.rm.common.resource.ResourceDescription;
import org.openehr.rm.common.resource.ResourceDescriptionItem;

import com.adl.domain.AdlFileParserDescription;
import com.adl.domain.AdlFileParserDescriptionDetails;

public class ArchetypeDescription<T extends AdlFileParserDescription> extends Parsed<T>{

	private Archetype archetype;
	
	private T description;

	public ArchetypeDescription(Archetype archetype,T description){
		super(archetype);
		this.archetype=archetype;
		this.description=description;
	}
	

	@Override
	public T parseArchetype() {
		// TODO Auto-generated method stub
		ResourceDescription resourceDescription = archetype.getDescription();
		description.setOriginalAuthor(resourceDescription.getOriginalAuthor());
		description.setOtherContributors(resourceDescription.getOtherContributors());
		description.setLifecycleState(resourceDescription.getLifecycleState());
		description.setOtherDetails(escapteDollarAndDotInOtherDetails(resourceDescription.getOtherDetails()));
		description.setDetails(parseAdlFileParserDescriptionDetails(resourceDescription.getDetails()));
		return description;
	}
	
	private Map<String,String> escapteDollarAndDotInOtherDetails(Map<String,String> otherDetails){
		Map<String, String> newMap=new HashMap<String,String>();
		for (Map.Entry<String, String> entry : otherDetails.entrySet())
		{
			String key=entry.getKey();
			String result=key.replace(".", "[dot]");
			newMap.put(result, entry.getValue());
		}
		return newMap;
	}
	
	private List<AdlFileParserDescriptionDetails> parseAdlFileParserDescriptionDetails(List<ResourceDescriptionItem> items){
		List<AdlFileParserDescriptionDetails> detailsList=new ArrayList<AdlFileParserDescriptionDetails>();
		for(ResourceDescriptionItem item:items){
			AdlFileParserDescriptionDetails details=new AdlFileParserDescriptionDetails();
			details.setLanguage(item.getLanguage().getCodeString());
			details.setPurpose(item.getPurpose());
			details.setKeywords(item.getKeywords());
			details.setUse(item.getUse());
			details.setCopyright(item.getCopyright());
			details.setOriginalResourceUri(item.getOriginalResourceUri());
			details.setOtherDetails(item.getOtherDetails());
			detailsList.add(details);
		}

		return detailsList;
	}
	
	
}
