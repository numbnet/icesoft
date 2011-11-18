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

package org.icefaces.ace.component.accordion;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.el.MethodExpression;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;
import org.icefaces.ace.meta.annotation.Expression;

@Component(
        tagName         = "accordion",
        componentClass  = "org.icefaces.ace.component.accordion.Accordion",
        rendererClass   = "org.icefaces.ace.component.accordion.AccordionRenderer",
        generatedClass  = "org.icefaces.ace.component.accordion.AccordionBase",
        extendsClass    = "javax.faces.component.UIPanel",
        componentType   = "org.icefaces.ace.component.Accordion",
        rendererType    = "org.icefaces.ace.component.AccordionRenderer",
		componentFamily = "org.icefaces.ace.component",
		tlddoc = "Container component that displays content in a stacked format."
        )

@ResourceDependencies({
	@ResourceDependency(library="icefaces.ace", name="jquery/ui/jquery-ui.css"),
	@ResourceDependency(library="icefaces.ace", name="jquery/jquery.js"),
	@ResourceDependency(library="icefaces.ace", name="jquery/ui/jquery-ui.js"),
	@ResourceDependency(library="icefaces.ace", name="core/core.js"),
	@ResourceDependency(library="icefaces.ace", name="accordion/accordion.js")
})

public class AccordionMeta extends UIPanelMeta {

	@Property(tlddoc="Name of the widget to access client side api")
	private String widgetVar;
	
	@Property(tlddoc="Index of the active pane.", defaultValue="0")
	private int activeIndex;
	
	@Property(tlddoc="Inline style of the container element.")
	private String style;
	
	@Property(tlddoc="Style class of the container element.")
	private String styleClass;
	
	@Property(tlddoc="Disables or enables the accordion.", defaultValue="false")
	private boolean disabled;
	
	@Property(tlddoc="Effect to use when toggling the panes.", defaultValue="slide")
	private String effect;
	
	@Property(tlddoc="When enabled, pane with highest content is used to calculate the height.", defaultValue="true")
	private boolean autoHeight;
	
	@Property(tlddoc="Defines if accordion can be collapsed all together.", defaultValue="false")
	private boolean collapsible;
	
	@Property(tlddoc="When enabled, accordion fills the height of its parent container.", defaultValue="false")
	private boolean fillSpace;
	
	@Property(tlddoc="Client side event to toggle the panes.")
	private String event;
	
	@Property(tlddoc="Server side listener to invoke when active pane changes", expression= Expression.METHOD_EXPRESSION, methodExpressionArgument="org.icefaces.ace.event.AccordionPaneChangeEvent")
	private MethodExpression paneChangeListener;
	
	@Property(tlddoc="Component(s) to update with ajax after dynamic pane change.")
	private String onPaneChangeUpdate;
	
	@Property(tlddoc="Client side callback to invoke when active pane changes.")
	private String onPaneChange;
	
	@Property(tlddoc="Defines the toggle mode.", defaultValue="false")
	private boolean dynamic;
	
	@Property(tlddoc="Defines if activating a dynamic pane should load the contents from server again.", defaultValue="true")
	private boolean cache;
}
