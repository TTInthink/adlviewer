package com.adl.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "adl_file_parser_identification")
public class AdlFileParserIdentification {

	private static final long serialVersionUID = 1L;

	private String adlVersion;
	
	private String uId;
	
	private String controlled;

	public String getAdlVersion() {
		return adlVersion;
	}

	public void setAdlVersion(String adlVersion) {
		this.adlVersion = adlVersion;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public String getControlled() {
		return controlled;
	}

	public void setControlled(String controlled) {
		this.controlled = controlled;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	
}
