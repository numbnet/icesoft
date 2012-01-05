/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.ace.component.dnd;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.el.MethodExpression;
import java.lang.reflect.Method;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
    tagName = "droppable",
    componentClass = "org.icefaces.ace.component.dnd.Droppable",
    generatedClass = "org.icefaces.ace.component.dnd.DroppableBase",
    rendererClass = "org.icefaces.ace.component.dnd.DroppableRenderer",
    extendsClass = "javax.faces.component.UIComponentBase",
    componentType = "org.icefaces.ace.component.Droppable",
    rendererType = "org.icefaces.ace.component.DroppableRenderer",
    componentFamily = "org.icefaces.ace.Droppable",
    tlddoc = "Enables an Id-specified component as an area that can have Droppable moved onto it to raise events."
)
@ClientBehaviorHolder(events = {
	@ClientEvent(name="drop", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all")
}, defaultEvent="drop")
@ResourceDependencies({
	@ResourceDependency(library="icefaces.ace", name="util/ace-jquery.js"),
	@ResourceDependency(library="icefaces.ace", name="util/ace-components.js")
})
public class DroppableMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "The JavaScript component instance variable name.")
    String widgetVar;
    @Property(name = "for",
              tlddoc = "Id of the component to add droppable behavior to.")
    String forValue;

    @Property(tlddoc = "Class to apply to droppable when an acceptable draggable is hovering.")
    String hoverStyleClass;
    @Property(tlddoc = "Class to apply to droppable when an acceptable draggable is dropped.")
    String activeStyleClass;

    @Property(tlddoc = "JQuery selector to define the set of acceptable draggables.")
    String accept;
    @Property(tlddoc = "This keyword allows sets of dragabbles and droppables to be associated with each other exclusively.")
    String scope;
    @Property(tlddoc = "This arbitrary keyword specifies the method of checking if a draggable is 'over' the droppable, possible values include: fit (drag must be smaller), intersect (drag must cover > 50%). pointer (cursor must be inside drop) and touch (any of the drop touches).")
    String tolerance;
    @Property(tlddoc = "Allows you to set the ID of an ACE UIData component and that will provide a Java object as input for the drop event. That object is the defined by matching the sibling index of the droppable to an object at that index in the UIData component backing.")
    String datasource;

    @Property(tlddoc = "ID of DOM node to re-render following drop events.")
    String onDropUpdate;
    @Property(tlddoc = "Javascript to execute when a drop event occurs.")
    String onDrop;

    @Property(tlddoc = "If enabled, no draggables will activate this droppable.")
    boolean disabled;

    @Property(expression = Expression.METHOD_EXPRESSION,
              methodExpressionArgument = "org.icefaces.ace.event.DragDropEvent",
              tlddoc = "MethodExpression reference to a method called whenever a draggable is moved into this droppable. The method receives a single argument, DragDropEvent.")
    MethodExpression dropListener;
}
