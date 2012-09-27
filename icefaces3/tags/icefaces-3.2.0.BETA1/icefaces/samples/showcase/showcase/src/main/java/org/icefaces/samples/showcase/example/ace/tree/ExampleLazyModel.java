package org.icefaces.samples.showcase.example.ace.tree;

import org.icefaces.ace.model.tree.LazyNodeDataModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
 * Time: 1:34 PM
 */
public class ExampleLazyModel extends LazyNodeDataModel<LocationNodeImpl> {
    private List<LocationNodeImpl> treeRoots = Arrays.asList(TreeDataFactory.getTreeRoots().clone());

    @Override
    public List<LocationNodeImpl> loadChildrenForNode(LocationNodeImpl node) {
        if (node == null)
            return treeRoots;

        if (treeRoots.get(0) == node)
            return treeRoots.get(0).children;

        for (LocationNodeImpl child : treeRoots.get(0).children)
            if (child == node) return child.children;

        return Collections.emptyList();
    }
}
