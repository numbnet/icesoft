package org.icefaces.ace.model.tree;

import org.icefaces.ace.model.SimpleEntry;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <p/>
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * User: Nils
 * Date: 2012-08-20
 * Time: 12:16 PM
 */
public class NodeCollectionToNodeEntryIterator<V> implements Iterator<Map.Entry<NodeKey, V>> {
    Iterator<V> iter;
    KeySegmentConverter converter;
    NodeKey parentKey;

    public NodeCollectionToNodeEntryIterator(KeySegmentConverter converter, NodeKey key, List<V> nodes) {
        parentKey = key;
        this.converter = converter;
        iter = nodes.iterator();
    }

    public boolean hasNext() {
        return iter.hasNext();
    }

    public Map.Entry<NodeKey, V> next() {
        V node = iter.next();
        return new SimpleEntry<NodeKey, V>(parentKey.append(converter.getSegment(node)), node);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
