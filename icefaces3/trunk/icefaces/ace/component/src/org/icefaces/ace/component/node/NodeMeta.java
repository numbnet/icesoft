package org.icefaces.ace.component.node;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

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
 * Date: 2012-08-21
 * Time: 4:48 PM
 */
@Component(
        tagName = "node",
        componentClass = "org.icefaces.ace.component.node.Node",
        generatedClass = "org.icefaces.ace.component.node.NodeBase",
        extendsClass   = "javax.faces.component.UIComponentBase",
        componentType  = "org.icefaces.ace.component.Node",
        componentFamily = "org.icefaces.ace.Node",
        tlddoc = "")
@ResourceDependencies({
        @ResourceDependency(library="icefaces.ace", name="util/ace-jquery.js")
})
@ClientBehaviorHolder(events = {
        @ClientEvent(name="select",
                javadoc="Fired when a row or cell is selected on the DataTable.",
                tlddoc="Fired when a row or cell is selected on the DataTable.",
                defaultRender="@all", defaultExecute="@this"),

        @ClientEvent(name="deselect",
                javadoc="Fired when a row or cell is deselected on the DataTable.",
                tlddoc="Fired when a row or cell is deselected on the DataTable.",
                defaultRender="@all", defaultExecute="@this")
},
        defaultEvent = "select"
)
public class NodeMeta extends UIComponentBaseMeta {
    @Property
    String type;
}
