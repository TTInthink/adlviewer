package com.adl.domain;

import java.io.Serializable;

public class AdlOptions implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public AdlOptions(String id,String name){
		this.id=id;
		this.name=name;
	}
	private String id;
	private String name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
