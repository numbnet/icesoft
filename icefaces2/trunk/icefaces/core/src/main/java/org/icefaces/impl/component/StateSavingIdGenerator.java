package org.icefaces.impl.component;

public class StateSavingIdGenerator {

	private static StateSavingIdGenerator instance = null;
	
	public static StateSavingIdGenerator get() {
	
		if (instance == null) {
			instance = new StateSavingIdGenerator();
		}
		
		return instance;
	}
	
	private StateSavingIdGenerator() { }
	
	private int count = 0;
	
	public String getNewId() {
		
		return String.valueOf(count++);
	}
}