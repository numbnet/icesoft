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

package com.icesoft.faces.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractCopyingAttributeMap extends HashMap {

    protected void initialize() {
        Enumeration e = getAttributeNames();
        while (e.hasMoreElements()) {
            String key = String.valueOf(e.nextElement());
            Object value = getAttribute(key);
            super.put(key, value);
        }
    }

    public Object put(Object key, Object value) {
        setAttribute(String.valueOf(key), value);
        return super.put(key, value);
    }

    public void putAll(Map map) {
        Iterator i = map.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            put(entry.getKey(), entry.getValue());
        }
    }

    public Object remove(Object o) {
        removeAttribute((String) o);
        return super.remove(o);
    }

    public void clear() {
        //copy the enumeration to avoid concurrency problems
        Iterator i = new ArrayList(Collections.list(getAttributeNames())).iterator();
        while (i.hasNext()) {
            remove(i.next());
        }
    }

    public abstract Enumeration getAttributeNames();

    public abstract Object getAttribute(String name);

    public abstract void setAttribute(String name, Object value);

    public abstract void removeAttribute(String name);
}
