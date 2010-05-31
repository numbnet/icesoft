package com.icefaces.project.memory.util;

import java.util.Random;

public class Randomizer extends Random {
	private static final long serialVersionUID = -8323038414458263329L;
	
	private static Randomizer singleton = null;
    
    private Randomizer() {
    	super(System.currentTimeMillis());
    }
    
    public static synchronized Randomizer getInstance() {
        if (singleton == null) {
            singleton = new Randomizer();
        }
        
        return(singleton);
    }
}
