/*
 * Original Code developed and contributed by Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: (ICE-6978) Used JSONBuilder to add the functionality of escaping JS output.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 */
package org.icefaces.ace.component.tooltip;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.render.MandatoryResourceComponent;

import java.util.Map;

@MandatoryResourceComponent(tagName="tooltip", value="org.icefaces.ace.component.tooltip.Tooltip")
public class TooltipRenderer extends CoreRenderer {

    @Override
    public void decode(FacesContext facesContext, UIComponent component) {
        Tooltip tooltip = (Tooltip) component;
        String clientId = tooltip.getClientId(facesContext);
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();

		//if(params.containsKey(clientId)) {
        if (params.containsKey(clientId + "_displayListener")) {
            if (tooltip.getDisplayListener() != null) {
                tooltip.getDisplayListener().invoke(facesContext.getELContext(), null);
            }
        } else {
            //RequestContext.getCurrentInstance().addCallbackParam(tooltip.getClientId(facesContext) + "_value", tooltip.getValue());
        }
        //FacesContext.getCurrentInstance().renderResponse();
        //}
        decodeBehaviors(facesContext, tooltip);
    }

    @Override
	public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
		Tooltip tooltip = (Tooltip) component;
		
		encodeScript(facesContext, tooltip);
		if(tooltip.getValue() == null) {
			ResponseWriter writer = facesContext.getResponseWriter();
			String clientId = tooltip.getClientId(facesContext);

			writer.startElement("div", null);
			writer.writeAttribute("id", clientId + "_content", null);
			writer.writeAttribute("style", "display:none;", null);
			
			renderChildren(facesContext, tooltip);

			writer.endElement("div");
		}
	}

	protected void encodeScript(FacesContext facesContext, Tooltip tooltip) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		boolean global = tooltip.isGlobal();
		String owner = getTarget(facesContext, tooltip);
		String clientId = tooltip.getClientId(facesContext);

        writer.startElement("span",null);
        writer.writeAttribute("id", clientId, null);

		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);

        writer.write("jQuery(function() {");

		writer.write(this.resolveWidgetVar(tooltip) + " = new ");

		JSONBuilder jb = JSONBuilder.create();
		jb.beginFunction("ice.ace.Tooltip")
			.beginMap()
				.entry("global", global)
				.entry("id", clientId)
				.entry("displayListener", (tooltip.getDisplayListener() != null));
		if (tooltip.isSpeechBubble()) jb.entry("speechBubble", true);
		String skinName = tooltip.getSkinName();
		if (skinName != null && !"".equals(skinName) && !"theme".equalsIgnoreCase(skinName)) jb.entry("skinName", skinName.toLowerCase());

		if(!global) {
			jb.entry("forComponent", owner);
			writer.write(jb.toString());
			writer.write(",content:");
			if(tooltip.getValue() == null)
				writer.write("document.getElementById('" + clientId + "_content').innerHTML");
			else {
				writer.write("'");
				writer.write(ComponentUtils.getStringValueToRender(facesContext, tooltip).replaceAll("'", "\\\\'"));
				writer.write("'");
			}

			writer.write(",");
		} else {
			writer.write(jb.toString());
			writer.write(",");
		}

		jb = JSONBuilder.create();
		//Events
		jb.beginMap("show")
			.beginMap("when")
				.entry("event", tooltip.getShowEvent())
			.endMap()
			.entry("delay", tooltip.getShowDelay())
			.beginMap("effect")
				.entry("length", tooltip.getShowEffectLength())
				.entry("type", tooltip.getShowEffect())
			.endMap()
		.endMap();

		jb.beginMap("hide")
			.beginMap("when")
				.entry("event", tooltip.getHideEvent())
			.endMap()
			.entry("delay", tooltip.getHideDelay())
			.beginMap("effect")
				.entry("length", tooltip.getHideEffectLength())
				.entry("type", tooltip.getHideEffect())
			.endMap()
		.endMap();

		//Position
		jb.beginMap("position");
        String container = owner == null ? "document.body" : "jQuery(ice.ace.escapeClientId('" + owner +"')).parent()";
        jb.entry("container", container, true)
			.beginMap("corner")
				.entry("target", tooltip.getTargetPosition())
				.entry("tooltip", tooltip.getPosition())
			.endMap()
		.endMap();

        encodeClientBehaviors(facesContext, tooltip, jb);
		jb.endMap().endFunction();
		writer.write(jb.toString());

		writer.write("});");

        writer.endElement("script");
        writer.endElement("span");
	}

	protected String getTarget(FacesContext facesContext, Tooltip tooltip) {
		if(tooltip.isGlobal())
			return null;
		else {
			String _for = tooltip.getFor();

			String forElement = tooltip.getForElement();
			if(_for != null) {
				UIComponent forComponent = tooltip.findComponent(_for);
				if(forComponent == null)
					throw new FacesException("Cannot find component \"" + _for + "\" in view.");
				else
					return forComponent.getClientId(facesContext);

			} else if(forElement != null) {
				return forElement;
			} else {
				return tooltip.getParent().getClientId(facesContext);
			}
		}
	}

	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		//Rendering happens on encodeEnd
	}

	public boolean getRendersChildren() {
		return true;
	}
}