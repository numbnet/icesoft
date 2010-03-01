/*
 * Version: MPL 1.1
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
 */

package org.icepush;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class VetoedSet implements Set {
    private HashMap items = new HashMap();

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public boolean contains(Object o) {
        return items.containsKey(o);
    }

    public Iterator iterator() {
        return items.keySet().iterator();
    }

    public Object[] toArray() {
        return items.keySet().toArray();
    }

    public Object[] toArray(Object[] a) {
        return items.keySet().toArray(a);
    }

    public boolean add(Object o) {
        Integer count = (Integer) items.get(o);
        if (count == null) {
            items.put(o, 1);
            return true;
        } else {
            items.put(o, count + 1);
            return false;
        }
    }

    public boolean remove(Object o) {
        Integer count = (Integer) items.get(o);
        if (count == null) {
            return false;
        } else {
            if (count > 1) {
                items.put(o, count - 1);
            } else {
                items.remove(o);
            }
            return true;
        }
    }

    public boolean containsAll(Collection c) {
        return items.keySet().containsAll(c);
    }

    public boolean addAll(Collection c) {
        boolean changed = false;
        for (Object i : c) {
            changed |= add(i);
        }
        return changed;
    }

    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection c) {
        boolean changed = false;
        for (Object i : c) {
            changed |= remove(i);
        }
        return changed;
    }

    public void clear() {
        items.clear();
    }
}
