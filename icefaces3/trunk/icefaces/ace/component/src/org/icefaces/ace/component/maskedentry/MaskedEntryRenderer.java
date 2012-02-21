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
package org.icefaces.ace.component.maskedentry;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.ace.renderkit.InputRenderer;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.Utils;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.render.MandatoryResourceComponent;

@MandatoryResourceComponent(tagName="maskedEntry", value="org.icefaces.ace.component.maskedentry.MaskedEntry")
public class MaskedEntryRenderer extends InputRenderer {
	
	@Override
	public void decode(FacesContext context, UIComponent component) {
		MaskedEntry maskedEntry = (MaskedEntry) component;

        if(maskedEntry.isDisabled() || maskedEntry.isReadonly()) {
            return;
        }

        decodeBehaviors(context, maskedEntry);

		String clientId = maskedEntry.getClientId(context);
		String submittedValue = (String) context.getExternalContext().getRequestParameterMap().get(clientId);

        if(submittedValue != null) {
            maskedEntry.setSubmittedValue(submittedValue);
        }
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		MaskedEntry maskedEntry = (MaskedEntry) component;
		
		encodeMarkup(context, maskedEntry);
		encodeScript(context, maskedEntry);
	}
	
	protected void encodeScript(FacesContext context, MaskedEntry maskedEntry) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = maskedEntry.getClientId(context);
		
		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);

        writer.write(this.resolveWidgetVar(maskedEntry) + " = new ");
		
		JSONBuilder jb = JSONBuilder.create();
		jb.beginFunction("ice.ace.InputMask")
			.item(clientId)
			.beginMap()
				.entry("mask", maskedEntry.getMask());

        String placeHolder = maskedEntry.getPlaceHolder();
		if(placeHolder!=null) {
			jb.entry("placeholder", placeHolder);
        }

        encodeClientBehaviors(context, maskedEntry, jb);

        if(!themeForms()) {
            jb.entry("theme", false);
        }

		jb.endMap().endFunction();
		writer.write(jb.toString());
	
		writer.endElement("script");
	}
	
	protected void encodeMarkup(FacesContext context, MaskedEntry maskedEntry) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = maskedEntry.getClientId(context);
        String defaultClass = themeForms() ? MaskedEntry.THEME_INPUT_CLASS : MaskedEntry.PLAIN_INPUT_CLASS;
        String styleClass = maskedEntry.getStyleClass();
		
		writer.startElement("input", null);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("name", clientId, null);
		writer.writeAttribute("type", "text", null);
		
		String valueToRender = ComponentUtils.getStringValueToRender(context, maskedEntry);
		if(valueToRender != null) {
			writer.writeAttribute("value", valueToRender , null);
		}
		
		renderPassThruAttributes(context, maskedEntry, HTML.INPUT_TEXT_ATTRS);

        if(maskedEntry.isDisabled()) writer.writeAttribute("disabled", "disabled", "disabled");
        if(maskedEntry.isReadonly()) writer.writeAttribute("readonly", "readonly", "readonly");
		String style = maskedEntry.getStyle();
        if(style != null) writer.writeAttribute("style", style, "style");
		
		Utils.writeConcatenatedStyleClasses(writer, defaultClass, styleClass);

        writer.endElement("input");
	}
}