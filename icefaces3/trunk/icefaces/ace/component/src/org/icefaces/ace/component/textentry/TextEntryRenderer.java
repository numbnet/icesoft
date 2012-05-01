/*
 * Original Code Copyright Prime Technology.
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
package org.icefaces.ace.component.textentry;

import org.icefaces.ace.component.datetimeentry.DateTimeEntryRenderer;
import org.icefaces.ace.renderkit.InputRenderer;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.ace.util.Utils;
import org.icefaces.render.MandatoryResourceComponent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

@MandatoryResourceComponent(tagName="textEntry", value="org.icefaces.ace.component.textentry.TextEntry")
public class TextEntryRenderer extends InputRenderer {
	
	@Override
	public void decode(FacesContext context, UIComponent component) {
		TextEntry textEntry = (TextEntry) component;

        if(textEntry.isDisabled() || textEntry.isReadonly()) {
            return;
        }

        decodeBehaviors(context, textEntry);

		String clientId = textEntry.getClientId(context);
        Map<String,String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String submittedValue = requestParameterMap.get(clientId);
        if (submittedValue == null && requestParameterMap.get(clientId + "_promptLabel") != null) {
            submittedValue = "";
        }

        if(submittedValue != null) {
            textEntry.setSubmittedValue(submittedValue);
        }
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		TextEntry textEntry = (TextEntry) component;
		
		encodeMarkup(context, textEntry);
		encodeScript(context, textEntry);
	}
	
	protected void encodeScript(FacesContext context, TextEntry textEntry) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = textEntry.getClientId(context);
        String promptLabel = textEntry.getPromptLabel();
        if (promptLabel == null || promptLabel.trim().length() <= 0) {
            promptLabel = "";
        }

		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);

        writer.write(this.resolveWidgetVar(textEntry) + " = new ");
		
		JSONBuilder jb = JSONBuilder.create();
		jb.beginFunction("ice.ace.TextEntry")
			.item(clientId)
			.beginMap()
				.entry("promptLabel", promptLabel);

        encodeClientBehaviors(context, textEntry, jb);

        if(!themeForms()) {
            jb.entry("theme", false);
        }

		jb.endMap().endFunction();
		writer.write(jb.toString());
	
		writer.endElement("script");
	}
	
	protected void encodeMarkup(FacesContext context, TextEntry textEntry) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = textEntry.getClientId(context);
        String defaultClass = themeForms() ? TextEntry.THEME_INPUT_CLASS : TextEntry.PLAIN_INPUT_CLASS;
        String styleClass = textEntry.getStyleClass();

		writer.startElement("input", null);
		writer.writeAttribute("id", clientId, null);
//		writer.writeAttribute("name", clientId, null);
		writer.writeAttribute("type", "text", null);
		
		String valueToRender = ComponentUtils.getStringValueToRender(context, textEntry);
        String promptLabel = textEntry.getPromptLabel();
		if(valueToRender != null && valueToRender.trim().length() > 0) {
            writer.writeAttribute("name", clientId, null);
			writer.writeAttribute("value", valueToRender , null);
        } else if (promptLabel != null && promptLabel.trim().length() > 0) {
            writer.writeAttribute("name", clientId + "_promptLabel", null);
            writer.writeAttribute("value", promptLabel, null);
            defaultClass += " ui-prompt-label";
        }

        renderPassThruAttributes(context, textEntry, HTML.INPUT_TEXT_ATTRS);

        if(textEntry.isDisabled()) writer.writeAttribute("disabled", "disabled", "disabled");
        if(textEntry.isReadonly()) writer.writeAttribute("readonly", "readonly", "readonly");
		String style = textEntry.getStyle();
        if(style != null) writer.writeAttribute("style", style, "style");
		
		Utils.writeConcatenatedStyleClasses(writer, defaultClass, styleClass);

        writer.endElement("input");
	}
}