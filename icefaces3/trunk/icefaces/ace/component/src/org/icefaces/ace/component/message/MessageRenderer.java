/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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
package org.icefaces.ace.component.message;

import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.util.EnvUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

@MandatoryResourceComponent(tagName = "message", value = "org.icefaces.ace.component.message.Message")
public class MessageRenderer extends Renderer {

    private static int iconIndex = -1;
    private static String[] icons = new String[]{"notice", "info", "alert", "alert"};
    private static String[] states = new String[]{"highlight", "highlight", "error", "error"};
    private String sourceClass = this.getClass().getName();
    private Logger logger = Logger.getLogger(sourceClass);

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        Message message = (Message) component;
        String forId = (forId = message.getFor()) == null ? "" : forId.trim();
        String style = message.getStyle();
        String styleClass = "ui-faces-message" + ((styleClass = message.getStyleClass()) == null ? "" : " " + styleClass);
        boolean ariaEnabled = EnvUtils.isAriaEnabled(context);
        String sourceMethod = "encodeEnd";

        UIComponent forComponent = forId.equals("") ? null : message.findComponent(forId);
        if (forComponent == null) {
            logger.logp(Level.WARNING, sourceClass, sourceMethod, "'for' attribute value cannot be null or empty or non-existent id.");
            return;
        }
        Iterator messageIter = context.getMessages(forComponent.getClientId(context));

        writer.startElement("span", message);
        writer.writeAttribute("id", message.getClientId(), "id");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }
        if (ariaEnabled) {
            writer.writeAttribute("role", "alert", null);
            writer.writeAttribute("aria-atomic", "true", null);
            writer.writeAttribute("aria-live", "polite", null);
            writer.writeAttribute("aria-relevant", "all", null);
        }

        boolean rendered = false;
        FacesMessage facesMessage;
        while (messageIter.hasNext()) {
            facesMessage = (FacesMessage) messageIter.next();
            if (!facesMessage.isRendered() || message.isRedisplay()) {
                encodeMessage(writer, message, facesMessage, styleClass);
                rendered = true;
                break;
            }
        }
        if (!rendered) {
            writer.writeAttribute("class", styleClass, null);
        }
        writer.endElement("span");
    }

    private void encodeMessage(ResponseWriter writer, Message message, FacesMessage facesMessage, String styleClass) throws IOException {

        boolean showSummary = message.isShowSummary();
        boolean showDetail = message.isShowDetail();
        String summary = (null != (summary = facesMessage.getSummary())) ? summary : "";
        String detail = (null != (detail = facesMessage.getDetail())) ? detail : ""; // Mojarra defaults to summary. Not good.
        String text = ((showSummary ? summary : "") + " " + (showDetail ? detail : "")).trim();
        int ordinal = facesMessage.getSeverity().getOrdinal();
//        ordinal = iconIndex = ++iconIndex % 4;

        if (text.equals("")) {
            styleClass += " ui-empty-message";
        }
        writer.writeAttribute("class", styleClass + " ui-widget ui-corner-all ui-state-" + states[ordinal], null);

        writer.startElement("span", message);
        writer.writeAttribute("class", "ui-icon ui-icon-" + icons[ordinal], null);
        writer.endElement("span");

        writer.startElement("span", message);
        writer.writeAttribute("class", "ui-icon-padding", null);
        writer.endElement("span");

        if (!text.equals("")) {
            writer.writeText(text, message, null);
        }

        facesMessage.rendered();
    }
}
