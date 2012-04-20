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

package org.icefaces.ace.component.richtextentry;

import org.icefaces.ace.renderkit.InputRenderer;
import org.icefaces.render.MandatoryResourceComponent;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

@MandatoryResourceComponent(tagName="richTextEntry", value="com.icesoft.faces.component.richtextentry.RichTextEntry")
public class RichTextEntryRenderer extends InputRenderer {

    public void decode(FacesContext context, UIComponent component) {
		decodeBehaviors(context, component);
	}
	
	public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        RichTextEntry richTextEntry = (RichTextEntry) uiComponent;

        writer.startElement("div", null);
        writer.writeAttribute("id", clientId + "container", null);
		writer.writeAttribute("class", richTextEntry.getStyleClass(), null);
		if (richTextEntry.getStyle() != null) {
			writer.writeAttribute("style", richTextEntry.getStyle(), null);
		}

		writer.startElement("textarea", null);
		writer.writeAttribute("name", clientId, null);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("style", "display:none;", null);
		Object value = richTextEntry.getValue();
		if (value != null) {
			writer.writeText(value, null);
		}
		
		writer.endElement("textarea");
		writer.endElement("div");

		writer.startElement("span", null);
		writer.writeAttribute("id", clientId + "scrpt", null);
		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);
		String customConfig =  (richTextEntry.getCustomConfigPath() == null)? "": richTextEntry.getCustomConfigPath();
		writer.writeText("ice.ace.richtextentry.renderEditor('"+ clientId +"', '"+ richTextEntry.getToolbar() +"'," +
				"'"+ richTextEntry.getLanguage()+"'," +
				"'"+ richTextEntry.getSkin().toLowerCase()+"'," +
				"'"+ richTextEntry.getHeight() + "'," +
				"'"+ richTextEntry.getWidth() +"'," +
				"'"+ customConfig + "'," +
				richTextEntry.isSaveOnSubmit() + ", {p:''", null);
		encodeClientBehaviors(facesContext, richTextEntry);
		writer.writeText("});", null);
		writer.endElement("script");
		writer.endElement("span");
    }
}
