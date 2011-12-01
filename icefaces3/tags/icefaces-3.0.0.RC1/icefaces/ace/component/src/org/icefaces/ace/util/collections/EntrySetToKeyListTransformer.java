package org.icefaces.ace.util.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class EntrySetToKeyListTransformer {
    public static List transform(Collection o) {
        List<Object> keySet = new ArrayList<Object>();
        for (Map.Entry e : (Collection<Map.Entry>) o) {
            keySet.add(e.getKey());
        }
        return keySet;
    }
}