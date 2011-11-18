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

package org.icefaces.ace.component.panel;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;

@Component(
        tagName         = "panel",
        componentClass  = "org.icefaces.ace.component.panel.Panel",
        rendererClass   = "org.icefaces.ace.component.panel.PanelRenderer",
        generatedClass  = "org.icefaces.ace.component.panel.PanelBase",
        extendsClass    = "javax.faces.component.UIPanel",
        componentType   = "org.icefaces.ace.component.Panel",
        rendererType    = "org.icefaces.ace.component.PanelRenderer",
		componentFamily = "org.icefaces.ace.Panel",
		tlddoc = "Panel is a generic grouping component that also supports toggling, closing and options menu. Both Close and Toggle events can be listened on server side with ajax listeners."
        )
@ResourceDependencies({
	@ResourceDependency(library="icefaces.ace", name="jquery/ui/jquery-ui.css"),
	@ResourceDependency(library="icefaces.ace", name="wijmo/wijmo.css"),
	@ResourceDependency(library="icefaces.ace", name="panel/panel.css"),
	@ResourceDependency(library="icefaces.ace", name="jquery/jquery.js"),
	@ResourceDependency(library="icefaces.ace", name="jquery/ui/jquery-ui.js"),
	@ResourceDependency(library="icefaces.ace", name="wijmo/wijmo.js"),
	@ResourceDependency(library="icefaces.ace", name="core/core.js"),
	@ResourceDependency(library="icefaces.ace", name="panel/panel.js")
})

public class PanelMeta extends UIPanelMeta {

	@Property(tlddoc="Name of the widget to access client side api")
	private String widgetVar;
	
	@Property(tlddoc="Header text of the panel")
	private String header;
	
	@Property(tlddoc="Footer text of the panel")
	private String footer;
	
	@Property(tlddoc="Boolean value that specifies whether the panel's open and close states can be toggled", defaultValue="false")
	private boolean toggleable;
	
	@Property(tlddoc="Integer value that specifies the duration of the transition between states", defaultValue="1000")
	private int toggleSpeed;
	
	@Property(tlddoc="Style to apply to the container element")
	private String style;
	
	@Property(tlddoc="Style class to apply to the container element")
	private String styleClass;
	
	@Property(tlddoc="Boolean value that specifies whether the panel is collapsed", defaultValue="false")
	private boolean collapsed;
	
	@Property(tlddoc="Boolean value that specifies whether the panel has a close button", defaultValue="false")
	private boolean closable;
	
	@Property(tlddoc="Integer value that specifies the duration of the closing animation", defaultValue="1000")
	private int closeSpeed;
	
	@Property(tlddoc="Boolean value that specifies whether the panel is visible", defaultValue="true")
	private boolean visible;
    
}
