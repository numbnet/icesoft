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

package org.icefaces.ace.component.tooltip;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.baseMeta.UIOutputMeta;
import javax.el.MethodExpression;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;

@Component(
        tagName         = "tooltip",
        componentClass  = "org.icefaces.ace.component.tooltip.Tooltip",
        rendererClass   = "org.icefaces.ace.component.tooltip.TooltipRenderer",
        generatedClass  = "org.icefaces.ace.component.tooltip.TooltipBase",
        extendsClass    = "javax.faces.component.UIOutput",
        componentType   = "org.icefaces.ace.component.Tooltip",
        rendererType    = "org.icefaces.ace.component.TooltipRenderer",
		componentFamily = "org.icefaces.ace.Tooltip",
		tlddoc = "Tooltip features a rich tooltip display with various events, effects, customization options and inline content display along with skinning options."
        )
@ResourceDependencies({
	@ResourceDependency(library="icefaces.ace", name="jquery/jquery.js"),
	@ResourceDependency(library="icefaces.ace", name="core/core.js"),
	@ResourceDependency(library="icefaces.ace", name="tooltip/jquery.qtip-1.0.0-rc3.js"),
//    @ResourceDependency(library="icefaces.ace", name="tooltip/jquery.qtip.debug-1.0.0-rc3.js"),
	@ResourceDependency(library="icefaces.ace", name="tooltip/tooltip.js")
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="display", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all")
}, defaultEvent="display")

public class TooltipMeta extends UIOutputMeta {

	@Property(tlddoc="Name of the widget to access client side api")
	private String widgetVar;

	@Property(tlddoc="Boolean value that makes tooltip global, which uses title attributes of elements in page to create the tooltip.", defaultValue="false")
	private boolean global;

	@Property(tlddoc="The corner of the target element by which to position the tooltip by (default bottomRight)", defaultValue="bottomRight")
	private String targetPosition;

	@Property(tlddoc="The corner of the tooltip to position in relation to the target's corner (defult topLeft)", defaultValue="topLeft")
	private String position;

	@Property(tlddoc="The mouse event that the tooltip will be displayed (default mouseover)", defaultValue="mouseover")
	private String showEvent;

	@Property(tlddoc="The delay time of the tooltip display in milliseconds (default 140)", defaultValue="140")
	private int showDelay;

	@Property(tlddoc="The show effect of the tooltip (default fade)", defaultValue="fade")
	private String showEffect;

	@Property(tlddoc="Duration to display the show effect (default 500)", defaultValue="500")
	private int showEffectLength;

	@Property(tlddoc="The mouse event that the tooltip will be closed (default mouseout)", defaultValue="mouseout")
	private String hideEvent;

	@Property(tlddoc="The delay time of the tooltip hide in milliseconds (default 0)", defaultValue="0")
	private int hideDelay;

	@Property(tlddoc="The hide effect of the tooltip (default fade)", defaultValue="fade")
	private String hideEffect;

	@Property(tlddoc="Duration to display the hide effect (default 500)", defaultValue="500")
	private int hideEffectLength;

	@Property(name="for", tlddoc="Specifies the id of the component that will display the tooltip")
	private String forValue;

	@Property(tlddoc="Specifies the id of the element that will display the tooltip")
	private String forElement;

    @Property(expression = Expression.METHOD_EXPRESSION,
              tlddoc = "A server side listener to be invoked when the tooltip is about to be shown in the client.")
    private MethodExpression displayListener;
}
