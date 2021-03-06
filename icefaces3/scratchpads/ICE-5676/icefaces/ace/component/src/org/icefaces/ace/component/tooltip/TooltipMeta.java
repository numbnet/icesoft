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
		tlddoc = "The Tooltip is a component that displays a tooltip with various events, effects and customization options." +
                "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/Tooltip\">Tooltip Wiki Documentation</a>."
        )
@ResourceDependencies({
	@ResourceDependency(library = "icefaces.ace", name = "util/ace-jquery.js"),
	@ResourceDependency(library = "icefaces.ace", name = "util/ace-components.js")
//    @ResourceDependency(library="icefaces.ace", name="tooltip/jquery.qtip.debug-1.0.0-rc3.js"),
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="display", javadoc="Fired before the tooltip is shown (default event).", tlddoc="Fired before the tooltip is shown (default event).", defaultRender="@all", defaultExecute="@all")
}, defaultEvent="display")

public class TooltipMeta extends UIOutputMeta {

	@Property(tlddoc="Name of the widget to access client side api")
	private String widgetVar;

	@Property(tlddoc="Boolean value specifying whether to create tooltips for all elements with a title attribute using the title attribute value.", defaultValue="false")
	private boolean global;

	@Property(tlddoc="The corner of the target element by which to position the tooltip by. E.g. topLeft, bottomRight. See <a href=\"http://craigsworks.com/projects/qtip/docs/tutorials/#position\">positioning</a> for all possible values.", defaultValue="bottomRight")
	private String targetPosition;

	@Property(tlddoc="The corner of the tooltip to position in relation to the target's corner. E.g. topLeft, bottomRight. See <a href=\"http://craigsworks.com/projects/qtip/docs/tutorials/#position\">positioning</a> for all possible values.", defaultValue="topLeft")
	private String position;

	@Property(tlddoc="Event which will trigger the showing of the tooltip. Possible values are DOM events, such as \"mouseover\", documented under <a href=\"http://docs.jquery.com/Events/bind#typedatafn\">jQuery's Event: bind()</a>.", defaultValue="mouseover")
	private String showEvent;

	@Property(tlddoc="Time in milliseconds by which to delay the showing of the tooltip.", defaultValue="140")
	private int showDelay;

	@Property(tlddoc="Effect to use upon showing the tooltip e.g. fade, slide or grow.", defaultValue="fade")
	private String showEffect;

	@Property(tlddoc="Length of time in milliseconds the show effect will last for.", defaultValue="500")
	private int showEffectLength;

	@Property(tlddoc="Event which will trigger the hiding of the tooltip. Possible values are DOM events, such as \"mouseout\", documented under <a href=\"http://docs.jquery.com/Events/bind#typedatafn\">jQuery's Event: bind()</a>.", defaultValue="mouseout")
	private String hideEvent;

	@Property(tlddoc="Time in milliseconds by which to delay the hiding of the tooltip.", defaultValue="0")
	private int hideDelay;

	@Property(tlddoc="Effect to use upon hiding the tooltip e.g. fade, slide or grow.", defaultValue="fade")
	private String hideEffect;

	@Property(tlddoc="Length of time in milliseconds the hide effect will last for.", defaultValue="500")
	private int hideEffectLength;

	@Property(name="for", tlddoc="Specifies the id of the component that will display the tooltip. Ignored if \"global\" is true.")
	private String forValue;

	@Property(tlddoc="Specifies the id (id won't be converted to a client-side id) of the element that will display the tooltip. Applicable only if \"for\" component is not specified. Ignored if \"global\" is true.")
	private String forElement;

    @Property(expression = Expression.METHOD_EXPRESSION,
              tlddoc = "A server side listener to be invoked when the tooltip is about to be shown in the client.")
    private MethodExpression displayListener;
	
	@Property(tlddoc="Specifies whether the tooltip should be stylized as a speech bubble (i.e. with a speech bubble arrow tip pointing to the triggerer element).", defaultValue="false")
	private boolean speechBubble;
}
