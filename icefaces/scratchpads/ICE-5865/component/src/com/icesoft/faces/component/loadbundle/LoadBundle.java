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

package com.icesoft.faces.component.loadbundle;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.icesoft.faces.utils.MessageUtils;

public class LoadBundle extends UIOutput implements Serializable{
    private static final long serialVersionUID = 1L;
    public static final String COMPONENT_TYPE = "com.icesoft.faces.LoadBundle";
    public static final String COMPONENT_FAMILY = "com.icesoft.faces.LoadBundle";
    private String basename;
    private String var;
    transient private Locale oldLocale;
    transient private String oldBasename = new String();
    transient private ResourceBundle bundle;
    private Map map;
    
    public LoadBundle() {
        setRendererType(null);
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    public String getComponentType() {
        return COMPONENT_TYPE;
    }
    public void decode(FacesContext context) {
        context.getExternalContext().getRequestMap().put(getVar(), map); 
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
        setRendererType(null);
        super.encodeBegin(context);
        String newBasename = getBasename();
        Locale currentLocale = context.getViewRoot().getLocale();
        boolean reloadRequired = !((oldLocale != null) && 
                oldLocale.getLanguage().equals(currentLocale.getLanguage()))
            || !oldBasename.equals(newBasename);
        if (reloadRequired) {
            bundle = ResourceBundle.getBundle(newBasename.trim(),
                    currentLocale,
                    MessageUtils.getClassLoader(this)); 
            map = new SerializableMap();
            context.getExternalContext().getRequestMap().put(getVar(), map); 
        }
        oldBasename = newBasename;
        oldLocale = currentLocale;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    public String getBasename() {
        if (basename != null) {
            return basename;
        }
        ValueBinding vb = getValueBinding("basename");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }
    
    private transient Object values[];
    /**
     * <p>Gets the state of the instance as a <code>Serializable</code>
     * Object.</p>
     */
    public Object saveState(FacesContext context) {
        if(values == null){
            values = new Object[4];
        }
        values[0] = super.saveState(context);
        values[1] = basename;
        values[2] = var;
        values[3] = map;
        return ((Object) (values));
    }

    /**
     * <p>Perform any processing required to restore the state from the entries
     * in the state Object.</p>
     */
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        basename = (String) values[1];
        var = (String) values[2];    
        map = (Map) values[3];
    }    

    class SerializableMap implements Map, Serializable {
        private static final long serialVersionUID = 1L;

            public void clear() {
                throw new UnsupportedOperationException();
            }

            public boolean containsKey(Object key) {
                return (null == key)?  false : (null != bundle.getObject(key.toString())) ;
            }

            public boolean containsValue(Object value) {
                boolean found = false;
                Object currentValue = null;
                Enumeration keys = bundle.getKeys();
                while (keys.hasMoreElements()) {
                    currentValue = bundle.getObject((String) keys.nextElement());
                    if ( (value == currentValue) ||
                            ((null != currentValue) && currentValue.equals(value))) {
                        found = true;
                        break;
                    }
                }
                return found;
            }

            public Set entrySet() {
                HashMap entries = new HashMap();
                Enumeration keys = bundle.getKeys();
                while (keys.hasMoreElements()) {
                    Object key = keys.nextElement();
                    Object value = bundle.getObject((String)key);
                    entries.put(key, value);
                }
                return entries.entrySet();
            }

            public Object get(Object key) {
                if (null == key) return null;
                Object result = null;
                try {
                    result = bundle.getObject(key.toString());
                } catch (MissingResourceException mre) {
                    result = "???"+ key + "???";
                }
                return result;
            }

            public boolean isEmpty() {
                return !bundle.getKeys().hasMoreElements();
            }

            public Set keySet() {
                Set keySet = new HashSet();
                Enumeration keys = bundle.getKeys();
                while (keys.hasMoreElements()) {
                    keySet.add(keys.nextElement());
                }
                return keySet;
            }

            public Object put(Object key, Object value) {
                throw new UnsupportedOperationException();
            }

            public void putAll(Map t) {
                throw new UnsupportedOperationException();                }

            public Object remove(Object key) {
                throw new UnsupportedOperationException();
            }

            public int size() {
                int size = 0;
                Enumeration keys = bundle.getKeys();
                while (keys.hasMoreElements()) {
                    keys.nextElement();
                    size++;
                }
                return size;
            }

            public Collection values() {
                ArrayList values = new ArrayList();
                Enumeration keys = bundle.getKeys();
                while(keys.hasMoreElements()) {
                    values.add(bundle.getObject((String)keys.nextElement()));
                }
                return values;
            }
            
            public int hashCode() {
                return bundle.hashCode();
            }
            
        }
}
