package org.icepush.integration.icepushplace.client.model;

import java.io.Serializable;

/**
 * Client-side object representing a user and all their related information (name, mood, etc.)
 * This also tracks a key which is used during web service calls
 */
public class User implements Serializable {
	private static final long serialVersionUID = 7887673308558051901L;

	public static final int DEFAULT_KEY = -1;
	
	private int key;
	private String name;
	private String mood;
	private String mind;
	private String region;
	private String message;
	private String technology;
	
	public User() {
	}
	
	public User(String name, String mood, String mind, String region, String message, String technology) {
		this(DEFAULT_KEY, name, mood, mind, region, message, technology);
	}
	
	public User(int key, String name, String mood, String mind, String region, String message, String technology) {
		this.key = key;
		this.name = name;
		this.mood = mood;
		this.mind = mind;
		this.region = region;
		this.message = message;
		this.technology = technology;
	}	
	
	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
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
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public boolean hasKey() {
		return getKey() != DEFAULT_KEY;
	}
	
	public String toString() {
		return name + " using " + technology + " is " + mood + " in " + region + ", thinking '" + mind + "' and saying '" + message + "'.";
	}
}
