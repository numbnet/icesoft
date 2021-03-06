/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package com.icesoft.util.pooling;

import java.util.Map;
import java.util.Collections;
import java.util.LinkedHashMap;
import javax.faces.context.FacesContext;

import java.util.logging.Level;
import java.util.logging.Logger;

public class StringInternMapLRU {

    private static Logger log = Logger.getLogger("com.icesoft.faces.compat");
    private static final int DEFAULT_MAX_SIZE = 95000;
    
    private Map map;
    private int defaultSize;
    private String contextParam;
    private boolean disabled;

    public StringInternMapLRU() {
        this(DEFAULT_MAX_SIZE);
    }

    public StringInternMapLRU(int size) {
        this(size, "");
    }
    
    public StringInternMapLRU(String contextParam) {
        this(DEFAULT_MAX_SIZE, contextParam);
    }

    public StringInternMapLRU(int defaultSize, String contextParam) {

        this.defaultSize = defaultSize;
        this.contextParam = contextParam;
        this.disabled = false;
    }
    
    private void createMap() {
    
        int maxSize = defaultSize;

        if (contextParam != null) {
            String maxSizeParam = FacesContext.getCurrentInstance().getExternalContext().getInitParameter(contextParam);
            if (maxSizeParam != null && maxSizeParam.length() > 0) {
                int configuredMaxSize = 0;
                try {
                    configuredMaxSize = Integer.parseInt(maxSizeParam);
                } catch (Exception e) {
                    log.log(Level.SEVERE, "Couldn't parse context-param: " + contextParam + ".", e);
                }
                if (configuredMaxSize > 0) {
                    maxSize = configuredMaxSize;
                } else {
                    disabled = true;
                    return;
                }
            }
        }

        int capacity = ((maxSize * 4) / 3) + 10;
        final int finalSize = maxSize;

        map = Collections.synchronizedMap(new LinkedHashMap(capacity, 0.75f, true) {
            
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > finalSize;
            }
        });
    }

    public Object get(Object value) {

        // Thread unsafe check, to reduce synchronised locks 
        if (map == null && !disabled) { 
            synchronized(this) { 
                // Actual thread-safe check 
                if (map == null && !disabled) { 
                    createMap(); 
                } 
            } 
        } 
        if (disabled) { 
            return value; 
        } 
        if (value == null) { 
            return null; 
        } 
        Object pooledValue = map.get(value); 
        if (pooledValue != null) { 
            return pooledValue; 
        } else { 
            map.put(value, value); 
            return value; 
        }
    }
    
    public int getSize() {
    
        if(map == null || disabled) {
            return 0;
        } else {
            return map.size();
        }
    }
}