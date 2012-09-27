package org.icefaces.ace.model.tree;

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
 * Date: 2012-09-13
 * Time: 12:05 PM
 */
public class NodeModelLazyListKeyConverter<K> implements KeySegmentConverter<K> {
    LazyNodeDataModel<K> model;
    public NodeModelLazyListKeyConverter(LazyNodeDataModel<K> ks) {
        model = ks;
    }

    public Object getSegment(K node) {
        K parent = model.parentMap.get(node);
        Map<K, List<K>> childMap = model.childMap;
        List siblings;

        // If parent returns null, root children are returned
        siblings = childMap.get(parent);

        return siblings.indexOf(node);
    }

    public NodeKey parseSegments(String[] segments) {
        Integer[] indexes = new Integer[segments.length];

        for (int i = 0; i < segments.length; i++) {
            indexes[i] = Integer.parseInt(segments[i]);
        }
        return new NodeKey(indexes);
    }
}
