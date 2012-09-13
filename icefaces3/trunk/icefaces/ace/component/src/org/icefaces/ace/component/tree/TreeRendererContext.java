package org.icefaces.ace.component.tree;

import org.icefaces.ace.model.tree.LazyNodeDataModel;

import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

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
 * Date: 2012-08-14
 * Time: 3:00 PM
 */
public class TreeRendererContext {
    private Tree tree;
    private boolean expansion;
    private boolean selection;
    private boolean multipleSelection;
    private boolean lazy;
    private TreeSelectionMode treeSelectionMode;
    private TreeExpansionMode treeExpansionMode;
    private String dotURL;

    public TreeRendererContext(Tree tree) {
        this.tree = tree;
        expansion = tree.isExpansion();
        selection = tree.isSelection();
        multipleSelection = tree.isMultipleSelection();
        treeSelectionMode = tree.getSelectionMode();
        treeExpansionMode = tree.getExpansionMode();
        lazy = tree.getValue() instanceof LazyNodeDataModel;

        ResourceHandler rh = FacesContext.getCurrentInstance()
                .getApplication().getResourceHandler();

        dotURL = rh.createResource("dot.png","icefaces.ace/tree").getRequestPath();
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public boolean isSelection() {
        return selection;
    }

    public boolean isMultipleSelection() {
        return multipleSelection;
    }

    public TreeSelectionMode getSelectionMode() {
        return treeSelectionMode;
    }

    public TreeExpansionMode getExpansionMode() {
        return treeExpansionMode;
    }

    public boolean isExpansion() {
        return expansion;
    }

    public String getDotURL() {
        return dotURL;
    }

    public boolean isLazy() {
        return lazy;
    }
}
