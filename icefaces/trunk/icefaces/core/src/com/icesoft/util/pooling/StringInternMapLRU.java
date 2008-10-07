/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */

package com.icesoft.util.pooling;

import java.util.Map;
import java.util.Collections;
import java.util.LinkedHashMap;

public class StringInternMapLRU {

    private static final int DEFAULY_MAX_SIZE = 100000;
    private static final int DEFAULT_CAPACITY = ((DEFAULY_MAX_SIZE*4)/3) + 10;
    
    private Map map;

    public StringInternMapLRU() {
    
        map = Collections.synchronizedMap(new LinkedHashMap(DEFAULT_CAPACITY, 0.75f, true) {
        
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > DEFAULY_MAX_SIZE;
            }
        }
            );
    }

    public StringInternMapLRU(final int size) {
    
        int capacity = ((size*4)/3) + 10;
    
        map = Collections.synchronizedMap(new LinkedHashMap(capacity, 0.75f, true) {
        
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > size;
            }
        }
            );
    }
    
    public String get(String string) {

        if (string != null) {
            String pooledString = (String) map.get(string);
            if (pooledString != null) {
                return pooledString;
            } else {
                map.put(string, string);
                return string;
            }
        } else {
            return null;
        }
    }
}