/*
 * Copyright (c) 2011, ICEsoft Technologies Canada Corp.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation 
 * and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * 
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
        
        String item;
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
