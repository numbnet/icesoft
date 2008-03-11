package com.icesoft.faces.context;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

public abstract class AbstractCopyingAttributeMap extends HashMap {

    protected void initialize() {
        Enumeration e = getAttributeNames();
        while (e.hasMoreElements()) {
            String key = String.valueOf(e.nextElement());
            Object value = getAttribute(key);
            super.put(key, value);
        }
    }

    public Object put(Object o, Object o1) {
        setAttribute(String.valueOf(o), o1);
        return super.put(o, o1);
    }

    public void putAll(Map map) {
        Iterator i = map.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            setAttribute(String.valueOf(entry.getKey()), entry.getValue());
        }
        super.putAll(map);
    }

    public Object remove(Object o) {
        removeAttribute((String) o);
        return super.remove(o);
    }

    public void clear() {
        //copy the enumeration to avoid concurrency problems
        Iterator i = new ArrayList(Collections.list(getAttributeNames())).iterator();
        while (i.hasNext()) {
            removeAttribute(String.valueOf(i.next()));
        }
        super.clear();
    }

    public abstract Enumeration getAttributeNames();

    public abstract Object getAttribute(String name);

    public abstract void setAttribute(String name, Object value);

    public abstract void removeAttribute(String name);
}
