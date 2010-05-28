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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
 */

package com.icesoft.faces.component.util;

import java.util.HashMap;
import java.util.Map;
/*
 * This class is created to give a java interface to the comma separated values
 * support format: key!value, key!value, ....
 * Along with this class a javascript utility added names as "Ice.delimitedProperties" 
 * which allows to create above formated string via object based API.  
 */
public class DelimitedProperties {
    Map properties = new HashMap();
    public DelimitedProperties(String rawProps) {
        if (null == rawProps || "".equals(rawProps))return;
        String[] props = rawProps.split(",");
        for (int i=0; i < props.length; i++) {
            String prop = props[i];
            String key = prop.substring(0, prop.indexOf("!"));
            String value = prop.substring(prop.indexOf("!")+1); 
            properties.put(key, value);
        }
    }
    
    public String get(String key) {
        return (String)properties.get(key); 
    }
    
    public void set(String key, String value) {
        properties.put(key, value);
    }
    
    public Map getProperties() {
        return properties;
    }
}
