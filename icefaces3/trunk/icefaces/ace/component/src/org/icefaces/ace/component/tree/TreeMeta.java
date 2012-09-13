package org.icefaces.ace.component.tree;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.UIDataMeta;
import org.icefaces.ace.model.tree.KeySegmentConverter;
import org.icefaces.ace.model.tree.NodeKey;
import org.icefaces.ace.model.tree.NodeStateMap;
import org.icefaces.ace.model.tree.StateCreationCallback;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

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
 * Time: 2:02 PM
 */
@Component(
        tagName = "tree",
        componentClass = "org.icefaces.ace.component.tree.Tree",
        generatedClass = "org.icefaces.ace.component.tree.TreeBase",
        rendererClass  = "org.icefaces.ace.component.tree.TreeRenderer",
        extendsClass   = "javax.faces.component.UIData",
        componentType  = "org.icefaces.ace.component.Tree",
        rendererType   = "org.icefaces.ace.component.TreeRenderer",
        componentFamily = "org.icefaces.ace.Tree",
        tlddoc = "")
@ResourceDependencies({
        @ResourceDependency(library="icefaces.ace", name="util/ace-jquery.js"),
        @ResourceDependency(library="icefaces.ace", name="util/ace-components.js")
})
@ClientBehaviorHolder(events = {
        @ClientEvent(name="expand",
            javadoc="",
            tlddoc="",
            defaultRender="@all", defaultExecute="@this"),

        @ClientEvent(name="contract",
            javadoc="",
            tlddoc="",
            defaultRender="@all", defaultExecute="@this"),

        @ClientEvent(name="select",
            javadoc="",
            tlddoc="",
            defaultRender="@all", defaultExecute="@this"),

        @ClientEvent(name="deselect",
            javadoc="",
            tlddoc="",
            defaultRender="@all", defaultExecute="@this")
        },
        defaultEvent = "select"
)
public class TreeMeta extends UIDataMeta {
    @Property
    KeySegmentConverter keyConverter;

    // Either a list of or single TreeNode, MutableTreeNode.
    // Or a NodeDataModel object.
    @Property
    Object value;

    @Property(defaultValue = "false",
        defaultValueType = DefaultValueType.EXPRESSION)
    Boolean pagination;

    @Property
    Integer pageSize;

    // First node of the current page;
    @Property(defaultValue = "org.icefaces.ace.model.tree.NodeKey.ROOT_KEY",
            defaultValueType = DefaultValueType.EXPRESSION)
    NodeKey firstNode;

    @Property
    String nodeKeyVar;

    @Property(defaultValue = "nodeState", defaultValueType = DefaultValueType.STRING_LITERAL)
    String stateVar;

    @Property(defaultValue = "org.icefaces.ace.component.tree.TreeExpansionMode.server",
            defaultValueType = DefaultValueType.EXPRESSION)
    TreeExpansionMode expansionMode;

    @Property(expression = Expression.VALUE_EXPRESSION)
    String type;

    @Property(defaultValue = "false", defaultValueType = DefaultValueType.EXPRESSION)
    Boolean selection;

    @Property(defaultValue = "false", defaultValueType = DefaultValueType.EXPRESSION)
    Boolean expansion;

    @Property(defaultValue = "false", defaultValueType = DefaultValueType.EXPRESSION)
    Boolean multipleSelection;

    @Property(defaultValue = "org.icefaces.ace.component.tree.TreeSelectionMode.server",
            defaultValueType = DefaultValueType.EXPRESSION)
    TreeSelectionMode selectionMode;

    @Property
    NodeStateMap stateMap;

    @Property
    StateCreationCallback stateCreationCallback;

    // Default DOM event that causes toggle
    @Property(defaultValue = "click",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    String toggleEvent;

    // Enable dragging for the table.
    // Still has to be enabled for particular nodes.
    @Property
    Boolean dragging;
}
