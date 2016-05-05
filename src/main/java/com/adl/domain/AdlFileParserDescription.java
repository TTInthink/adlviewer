package com.adl.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "adl_file_parser_description")
public class AdlFileParserDescription implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Map<String, String> originalAuthor;
	
	private List<String> otherContributors;
	
	private String lifecycleState;
	
	private Map<String, String> otherDetails;
	
//	@DBRef
	private List<AdlFileParserDescriptionDetails> details;

	public Map<String, String> getOriginalAuthor() {
		return originalAuthor;
	}

	public void setOriginalAuthor(Map<String, String> originalAuthor) {
		this.originalAuthor = originalAuthor;
	}

	public List<String> getOtherContributors() {
		return otherContributors;
	}

	public void setOtherContributors(List<String> otherContributors) {
		this.otherContributors = otherContributors;
	}

	public String getLifecycleState() {
		return lifecycleState;
	}

	public void setLifecycleState(String lifecycleState) {
		this.lifecycleState = lifecycleState;
	}

	public Map<String, String> getOtherDetails() {
		return otherDetails;
	}

	public void setOtherDetails(Map<String, String> otherDetails) {
		this.otherDetails = otherDetails;
	}

	public List<AdlFileParserDescriptionDetails> getDetails() {
		return details;
	}

	public void setDetails(List<AdlFileParserDescriptionDetails> details) {
		this.details = details;
	}

	
}
