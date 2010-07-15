package org.icepush.integration.icepushplace.client.model;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1008168282470225034L;
	
	private String name;
	private String mood;
	private String mind;
	private String region;
	
	public User() {
	}
	
	public User(String name, String mood, String mind, String region) {
		this.name = name;
		this.mood = mood;
		this.mind = mind;
		this.region = region;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMood() {
		return mood;
	}
	
	public void setMood(String mood) {
		this.mood = mood;
	}
	
	public String getMind() {
		return mind;
	}
	
	public void setMind(String mind) {
		this.mind = mind;
	}
	
	public String getRegion() {
		return region;
	}
	
	public void setRegion(String region) {
		this.region = region;
	}
}
