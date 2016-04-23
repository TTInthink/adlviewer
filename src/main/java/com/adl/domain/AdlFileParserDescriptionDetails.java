package com.adl.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "adl_file_parser_description_details")
public class AdlFileParserDescriptionDetails implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    private String id;
	
	private String language;
	private String purpose;
	private List<String> keywords;
	private String use;
	private String misuse;
	private String copyright;
	private Map<String, String> originalResourceUri;
	private Map<String, String> otherDetails;
	
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public List<String> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}
	public String getMisuse() {
		return misuse;
	}
	public void setMisuse(String misuse) {
		this.misuse = misuse;
	}
	public String getCopyright() {
		return copyright;
	}
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	public Map<String, String> getOriginalResourceUri() {
		return originalResourceUri;
	}
	public void setOriginalResourceUri(Map<String, String> originalResourceUri) {
		this.originalResourceUri = originalResourceUri;
	}
	public Map<String, String> getOtherDetails() {
		return otherDetails;
	}
	public void setOtherDetails(Map<String, String> otherDetails) {
		this.otherDetails = otherDetails;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
