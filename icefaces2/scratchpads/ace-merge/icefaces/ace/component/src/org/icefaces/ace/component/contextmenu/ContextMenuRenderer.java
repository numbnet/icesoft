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
 * Code Modification 2: (ICE-6978) Used JSONBuilder to add the functionality of escaping JS output.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 * Contributors: ______________________
 */
package org.icefaces.ace.component.contextmenu;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.icefaces.ace.component.menu.AbstractMenu;
import org.icefaces.ace.component.menu.BaseMenuRenderer;

import org.icefaces.ace.component.menuitem.MenuItem;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.render.MandatoryResourceComponent;

@MandatoryResourceComponent("org.icefaces.ace.component.contextmenu.ContextMenu")
public class ContextMenuRenderer extends BaseMenuRenderer {

    protected void encodeScript(FacesContext context, AbstractMenu abstractMenu) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
        ContextMenu menu = (ContextMenu) abstractMenu;
		String widgetVar = this.resolveWidgetVar(menu);
		String clientId = menu.getClientId(context);
		String trigger = findTrigger(context, menu);
		
		writer.startElement("script", menu);
		writer.writeAttribute("type", "text/javascript", null);

        JSONBuilder json = JSONBuilder.create();
        writer.write("jQuery(function() {");
        
		writer.write(widgetVar + " = new ");
        json.beginFunction("ice.ace.ContextMenu").
            item(clientId).
            beginMap().
                entry("target", trigger, true).
                entry("zindex", menu.getZindex()).

                beginMap("animation").
                    entry("animated", menu.getEffect()).
                    entry("duration", menu.getEffectDuration()).
                endMap().

                entryNonNullValue("styleClass", menu.getStyleClass()).
                entryNonNullValue("style", menu.getStyle()).
            endMap().
        endFunction();

        writer.write(json.toString());
        writer.write("});");

		writer.endElement("script");
	}
	
    protected void encodeMarkup(FacesContext context, AbstractMenu abstractMenu) throws IOException{
		ResponseWriter writer = context.getResponseWriter();
        ContextMenu menu = (ContextMenu) abstractMenu;
		String clientId = menu.getClientId(context);

        writer.startElement("span", menu);
		writer.writeAttribute("id", clientId, "id");

		writer.startElement("ul", null);
		writer.writeAttribute("id", clientId + "_menu", null);

		for(UIComponent child : menu.getChildren()) {
			MenuItem item = (MenuItem) child;

			if(item.isRendered()) {
                writer.startElement("li", null);
                encodeMenuItem(context, item);
                writer.endElement("li");
			}
		}

		writer.endElement("ul");

        writer.endElement("span");
	}

    protected String findTrigger(FacesContext context, ContextMenu menu) {
		String trigger = null;
		String _for = menu.getFor();

		if(_for != null) {
			UIComponent forComponent = menu.findComponent(_for);

			if(forComponent == null)
				throw new FacesException("Cannot find component '" + _for + "' in view.");
			else {
                return "'" +  forComponent.getClientId(context) + "'";
			}
		}
		else {
			trigger = "document";
		}

		return trigger;
	}
}