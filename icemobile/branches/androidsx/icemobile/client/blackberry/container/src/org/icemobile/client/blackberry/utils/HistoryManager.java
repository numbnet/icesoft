/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */ 
package org.icemobile.client.blackberry.utils;

import java.util.Vector;

/**
 * Simple tool for maintaining the last N URLs visited. 
 * While the BrowserField has a BrowserFieldHistory class, 
 * it only supports the notion of forward/back, and doesn't 
 * provide a list of URLs for display 
 *
 */
public class HistoryManager {
    
    private Vector mUrls; 
    private int mListSize; 
    
    public HistoryManager( int size) {
        if (size <= 0) { 
            throw new IllegalArgumentException ("Illegal history size: " + size);
        }
        mUrls = new Vector();
        mListSize = size;
    }
    
    public void addLocation( String url ) {
        
        int idx = mUrls.indexOf( url ); 
        if (idx > -1) { 
        	mUrls.removeElementAt(idx); 
        } else { 
        	if (mUrls.size() == mListSize ) { 
        		mUrls.removeElementAt( mListSize - 1); 
        	}
        } 
        mUrls.insertElementAt(url, 0);
        
    }
    
    public String[] getHistoryLocations() {         
        String[] returnVal = new String[mUrls.size()]; 
        mUrls.copyInto( returnVal ); 
        return returnVal;
    }    
}
