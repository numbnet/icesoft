/*
 * Original Code developed and contributed by Prime Technology.
 * Subsequent Code Modifications Copyright 2011 ICEsoft Technologies Canada Corp. (c)
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
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */
package org.icefaces.ace.component.notificationpanel;

import org.icefaces.ace.util.JSONBuilder;
import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.render.MandatoryResourceComponent;

@MandatoryResourceComponent("org.icefaces.ace.component.notificationpanel.NotificationPanel")
public class NotificationPanelRenderer extends CoreRenderer {

    @Override
	public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
		NotificationPanel panel = (NotificationPanel) component;
		
		encodeMarkup(facesContext, panel);
		encodeScript(facesContext, panel);
	}
	
	protected void encodeMarkup(FacesContext facesContext, NotificationPanel panel) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
        String panelStyleClass = panel.getStyleClass();
        String styleClass = panelStyleClass == null ? "ui-notificationbar" : "ui-notificationbar " + panelStyleClass;
		UIComponent close = panel.getFacet("close");
		
		writer.startElement("div", panel);
		writer.writeAttribute("id", panel.getClientId(facesContext), null);
		writer.writeAttribute("class", styleClass, null);
        String style = panel.getStyle();
        if(style != null) writer.writeAttribute("style", style, null);
		
		if(close != null) {
			writer.startElement("span", null);
			writer.writeAttribute("class", "ui-notificationbar-close", null);
			writer.writeAttribute("onclick", this.resolveWidgetVar(panel) + ".hide()", null);
			renderChild(facesContext, close);
			writer.endElement("span");
		}

		renderChildren(facesContext, panel);
		
		writer.endElement("div");
	}

	private void encodeScript(FacesContext facesContext, NotificationPanel panel) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		String clientId = panel.getClientId(facesContext);
		
		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);
		
        JSONBuilder json = JSONBuilder.create();
		writer.write("jQuery(document).ready(function(){");

		writer.write(this.resolveWidgetVar(panel) + " = new ");
        json.beginFunction("ice.ace.NotificationBar").
            item(clientId).
            beginMap().
                entry("position", panel.getPosition()).
                entry("effect", panel.getEffect()).
                entry("effectSpeed", panel.getEffectSpeed());

                if(panel.isAutoDisplay())
                    json.entry("autoDisplay", true);

            json.endMap();
        json.endFunction();
        writer.write(json.toString());
		writer.write("});");
		
		writer.endElement("script");
	}

	public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
		//Do nothing
	}

	public boolean getRendersChildren() {
		return true;
	}
}