/*
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.icefaces.ace.component.resizable;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.el.MethodExpression;

import org.icefaces.ace.event.ResizeEvent;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;

@Component(
        tagName         = "resizable",
        componentClass  = "org.icefaces.ace.component.resizable.Resizable",
        rendererClass   = "org.icefaces.ace.component.resizable.ResizableRenderer",
        generatedClass  = "org.icefaces.ace.component.resizable.ResizableBase",
        extendsClass    = "javax.faces.component.UIComponentBase",
        componentType   = "org.icefaces.ace.component.Resizable",
        rendererType    = "org.icefaces.ace.component.ResizableRenderer",
		componentFamily = "org.icefaces.ace.Resizable",
		tlddoc = "Resizable component can give resizable behavior to any JSF component."
        )
@ResourceDependencies({
	@ResourceDependency(library="icefaces.ace", name="jquery/ui/jquery-ui.css"),
	@ResourceDependency(library="icefaces.ace", name="jquery/jquery.js"),
	@ResourceDependency(library="icefaces.ace", name="jquery/ui/jquery-ui.js"),
	@ResourceDependency(library="icefaces.ace", name="core/core.js"),
	@ResourceDependency(library="icefaces.ace", name="resizable/resizable.js")
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="resize", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all")
}, defaultEvent="resize")

public class ResizableMeta extends UIComponentBaseMeta {

	@Property(tlddoc="Name of the widget to access client side api")
	private String widgetVar;
	
	@Property(name="for", tlddoc="Specifies the id of the component to make resizable")
	private String forValue;
	
	@Property(tlddoc="Boolean value that specifies whether the aspect ratio of the component should be maintained when resizing", defaultValue="false")
	private boolean aspectRatio;
	
	@Property(tlddoc="Boolean value that specifies whether the resizable should use a proxy", defaultValue="false")
	private boolean proxy;
	
	@Property(tlddoc="Handles to use, any combination of 't', 'b', 'r', 'l', 'bl', 'br', 'tl', 'tr' is valid, shortcut \"all\" enables all handlers.")
	private String handles;
	
	@Property(tlddoc="Boolean value that specifies whether a ghost should be used to resize the component", defaultValue="false")
	private boolean ghost;
	
	@Property(tlddoc="Boolean value that specifies whether the resizing should be animated", defaultValue="false")
	private boolean animate;
	
	@Property(tlddoc="Specifies the effect to display when resizing the component (default swing)", defaultValue="swing")
	private String effect;
	
	@Property(tlddoc="specifies the duration of the effect", defaultValue="normal")
	private String effectDuration;
	
	@Property(tlddoc="Maximum width of the resizable", defaultValue="Integer.MAX_VALUE")
	private int maxWidth;
	
	@Property(tlddoc="Maximum height of the resizable", defaultValue="Integer.MAX_VALUE")
	private int maxHeight;
	
	@Property(tlddoc="Minimum width of the resizable", defaultValue="Integer.MIN_VALUE")
	private int minWidth;
	
	@Property(tlddoc="Minimum height of the resizable", defaultValue="Integer.MIN_VALUE")
	private int minHeight;
	
	@Property(tlddoc="Boolean value that specifies whether the resizable should be restricted to its parent's boundaries", defaultValue="false")
	private boolean containment;
	
	@Property(tlddoc="Size in pixels of the increments in which the resizable should increase/decrease its size", defaultValue="1")
	private int grid;
	
	@Property(tlddoc="Component(s) to update after AJAX call caused by resizing event.")
	private String onResizeUpdate;
	
	@Property(expression= Expression.METHOD_EXPRESSION, methodExpressionArgument="org.icefaces.ace.event.ResizeEvent", tlddoc="Action listener for the resizing event")
	private MethodExpression resizeListener;
	
	@Property(tlddoc="Javascript code to be executed when the user starts resizing the component")
	private String onStart;
	
	@Property(tlddoc="Javascript code to be executed when resizing is done")
	private String onResize;
	
	@Property(tlddoc="Javascript code to be executed when the user stops resizing the component")
	private String onStop;
    
}
