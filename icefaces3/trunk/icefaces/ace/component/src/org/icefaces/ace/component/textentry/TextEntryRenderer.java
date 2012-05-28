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
package org.icefaces.ace.component.textentry;

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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        String submittedValue = requestParameterMap.get(clientId + "_input");
        if (submittedValue == null && requestParameterMap.get(clientId + "_label") != null) {
            submittedValue = "";
        }

        if(submittedValue != null) {
            textEntry.setSubmittedValue(submittedValue);
        }
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		TextEntry textEntry = (TextEntry) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = textEntry.getClientId(context);

        writer.startElement("span", component);
        writer.writeAttribute("id", clientId, "clientId");

        String defaultClass = themeForms() ? TextEntry.THEME_INPUT_CLASS : TextEntry.PLAIN_INPUT_CLASS;
        String styleClass = textEntry.getStyleClass();

        defaultClass += getStateStyleClasses(textEntry);
        boolean required = textEntry.isRequired();

        String label = textEntry.getLabel();
        String labelPosition = textEntry.getLabelPosition();
        if (!labelPositionSet.contains(labelPosition)) labelPosition = DEFAULT_LABEL_POSITION;
        boolean hasLabel = !(labelPosition.equals(NONE_LABEL_POSITION) || isValueBlank(label));

        String indicator = required ? textEntry.getRequiredIndicator() : textEntry.getOptionalIndicator();
        String indicatorPosition = textEntry.getIndicatorPosition();
        if (!indicatorPositionSet.contains(indicatorPosition)) indicatorPosition = DEFAULT_INDICATOR_POSITION;
        boolean hasIndicator = !(indicatorPosition.equals(NONE_INDICATOR_POSITION) || isValueBlank(indicator));

        writer.startElement("span", textEntry);
        writer.writeAttribute("id", clientId + "_markup", null);

        writeLabelAndIndicatorBefore(writer, label, hasLabel, labelPosition, indicator, hasIndicator, indicatorPosition, required);

        writer.startElement("input", null);
        writer.writeAttribute("id", clientId + "_input", null);
        writer.writeAttribute("type", "text", null);

        String embeddedLabel = null;
        String nameToRender = clientId + "_input";
        String valueToRender = ComponentUtils.getStringValueToRender(context, textEntry);
        if ((valueToRender == null || valueToRender.trim().length() <= 0) && hasLabel && labelPosition.equals("inField")) {
            nameToRender = clientId + "_label";
            valueToRender = embeddedLabel = label;
            if (hasIndicator) {
                if (indicatorPosition.equals("labelLeft")) {
                    valueToRender = embeddedLabel = indicator + valueToRender;
                } else if (indicatorPosition.equals("labelRight")) {
                    valueToRender = embeddedLabel = valueToRender + indicator;
                }
            }
            defaultClass += " " + LABEL_STYLE_CLASS + "-infield";
        }
        writer.writeAttribute("name", nameToRender, null);
        writer.writeAttribute("value", valueToRender , null);

        renderPassThruAttributes(context, textEntry, HTML.INPUT_TEXT_ATTRS);

        if(textEntry.isDisabled()) writer.writeAttribute("disabled", "disabled", "disabled");
        if(textEntry.isReadonly()) writer.writeAttribute("readonly", "readonly", "readonly");
        String style = textEntry.getStyle();
        if(style != null) writer.writeAttribute("style", style, "style");

        Utils.writeConcatenatedStyleClasses(writer, defaultClass, styleClass);

        writer.endElement("input");

        writeLabelAndIndicatorAfter(writer, label, hasLabel, labelPosition, indicator, hasIndicator, indicatorPosition, required);

        writer.endElement("span");

        writer.startElement("span", textEntry);
        writer.writeAttribute("id", clientId + "_script", "clientId");
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);

        writer.write(this.resolveWidgetVar(textEntry) + " = new ");

        JSONBuilder jb = JSONBuilder.create();
        jb.beginFunction("ice.ace.TextEntry")
            .item(clientId)
            .beginMap()
                .entryNonNullValue("embeddedLabel", embeddedLabel);

        encodeClientBehaviors(context, textEntry, jb);

        if(!themeForms()) {
            jb.entry("theme", false);
        }
        jb.entry("autoTab", textEntry.isAutoTab() && textEntry.getMaxlength() > 0);
        StringBuilder sb = new StringBuilder();
        sb.append(indicatorPosition).append(labelPosition);
        jb.entry("hashCode", sb.toString().hashCode());

        jb.endMap().endFunction();
        writer.write(jb.toString());

        writer.endElement("script");
        writer.endElement("span");

        writer.endElement("span");
	}

    private void writeHiddenLabel(ResponseWriter writer, String label) throws IOException {
        writer.startElement("span", null);
        writer.writeAttribute("class", LABEL_STYLE_CLASS + " hidden", null);
        writer.write(label);
        writer.endElement("span");
    }

    private void writeLabel(ResponseWriter writer, String label, String labelPosition, String indicator, String indicatorPosition, boolean required, boolean hasIndicator) throws IOException {
        if (hasIndicator) {
            if (indicatorPosition.equals("labelLeft") || indicatorPosition.equals("labelRight")) {
                writer.startElement("span", null);
                writer.writeAttribute("class", LABEL_STYLE_CLASS + " " + LABEL_STYLE_CLASS + "-" + labelPosition, null);
            }
/*
            if (indicatorPosition.equals("labelTop") || indicatorPosition.equals("labelBottom")) {
                writer.startElement("span", null);
                writer.writeAttribute("style", "float:left;", null);
            }
*/
            if (indicatorPosition.equals("labelLeft")/* || indicatorPosition.equals("labelTop")*/) {
                writeIndicator(writer, required, indicator, indicatorPosition);
            }
/*
            if (indicatorPosition.equals("labelTop")) {
                writer.startElement("br", null);
                writer.endElement("br");
            }
*/
        }
        writer.startElement("span", null);
        if (!hasIndicator || (!indicatorPosition.equals("labelLeft") && !indicatorPosition.equals("labelRight"))) {
            writer.writeAttribute("class", LABEL_STYLE_CLASS + " " + LABEL_STYLE_CLASS + "-" + labelPosition, null);
        }
        writer.write(label);
        writer.endElement("span");
        if (hasIndicator) {
/*
            if (indicatorPosition.equals("labelBottom")) {
                writer.startElement("br", null);
                writer.endElement("br");
            }
*/
            if (indicatorPosition.equals("labelRight")/* || indicatorPosition.equals("labelBottom")*/) {
                writeIndicator(writer, required, indicator, indicatorPosition);
            }
/*
            if (indicatorPosition.equals("labelTop") || indicatorPosition.equals("labelBottom")) {
                writer.endElement("span");
            }
*/
            if (indicatorPosition.equals("labelLeft") || indicatorPosition.equals("labelRight")) {
                writer.endElement("span");
            }
        }
    }

    private void writeIndicator(ResponseWriter writer, boolean required, String indicator, String indicatorPosition) throws IOException {
        String class1 = "ui-" + (required ? "required" : "optional") + "-indicator";
        String class2;
        if (indicatorPosition.equals("labelLeft")) {
            class2 = class1 + "-label-left";
        } else if (indicatorPosition.equals("labelRight")) {
            class2 = class1 + "-label-right";
        } else {
            class2 = class1 + "-" + indicatorPosition;
        }
        writer.startElement("span", null);
        writer.writeAttribute("class", class1 + " " + class2, null);
        writer.write(indicator);
        writer.endElement("span");
    }
}