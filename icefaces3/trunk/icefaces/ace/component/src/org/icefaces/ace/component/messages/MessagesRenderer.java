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
package org.icefaces.ace.component.messages;

import org.icefaces.render.MandatoryResourceComponent;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

@MandatoryResourceComponent(tagName = "messages", value = "org.icefaces.ace.component.messages.Messages")
public class MessagesRenderer extends Renderer {

    private static int iconIndex = -1;
    private static String[] icons = new String[]{"notice", "info", "alert", "alert"};
    private static String[] states = new String[]{"highlight", "highlight", "error", "error"};

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        Messages messages = (Messages) component;
        String forId = messages.getFor();
        Iterator messageIter = Collections.EMPTY_LIST.iterator();

        if (forId == null) {
            if (messages.isGlobalOnly()) {
                messageIter = context.getMessages(null);
            } else {
                messageIter = context.getMessages();
            }
        } else {
            UIComponent forComponent = messages.findComponent(forId);
            if (forComponent != null) {
                messageIter = context.getMessages(forComponent.getClientId(context));
            }
        }
        writer.startElement("div", messages);
        writer.writeAttribute("id", messages.getClientId(), "id");
        writer.writeAttribute("class", "ui-faces-messages ui-widget", null);
        while (messageIter.hasNext()) {
            FacesMessage facesMessage = (FacesMessage) messageIter.next();
            if (!facesMessage.isRendered() || messages.isRedisplay()) {
                encodeMessage(writer, messages, facesMessage);
            }
        }
/*
        if ("table".equals(messages.getLayout())) {
            encodeMessageTable(writer, messages, messageIter);
        } else {
            encodeMessageList(writer, messages, messageIter);
        }
*/
        writer.endElement("div");
    }

    private void encodeMessageTable(ResponseWriter writer, Messages messages, Iterator messageIter) throws IOException {

        boolean wroteTable = false;
        while (messageIter.hasNext()) {
            FacesMessage facesMessage = (FacesMessage) messageIter.next();
            if (facesMessage.isRendered() && !messages.isRedisplay()) {
                continue;
            }
            if (!wroteTable) {
                writer.startElement("table", messages);
                wroteTable = true;
            }
            writer.startElement("tr", messages);
            writer.startElement("td", messages);
            encodeMessage(writer, messages, facesMessage);
            writer.endElement("td");
            writer.endElement("tr");
        }
        if (wroteTable) {
            writer.endElement("table");
        }
    }

    private void encodeMessageList(ResponseWriter writer, Messages messages, Iterator messageIter) throws IOException {

        boolean wroteSpan = false;
        while (messageIter.hasNext()) {
            FacesMessage facesMessage = (FacesMessage) messageIter.next();
            if (facesMessage.isRendered() && !messages.isRedisplay()) {
                continue;
            }
            if (!wroteSpan) {
                writer.startElement("ul", messages);
                wroteSpan = true;
            }
            writer.startElement("li", messages);
            encodeMessage(writer, messages, facesMessage);
            writer.endElement("li");
        }
        if (wroteSpan) {
            writer.endElement("ul");
        }
    }

    private void encodeMessage(ResponseWriter writer, Messages messages, FacesMessage facesMessage) throws IOException {

        boolean showSummary = messages.isShowSummary();
        boolean showDetail = messages.isShowDetail();
        String summary = (null != (summary = facesMessage.getSummary())) ? summary : "";
        String detail = (null != (detail = facesMessage.getDetail())) ? detail : ""; // Mojarra defaults to summary. Not good.
        String text = ((showSummary ? summary : "") + " " + (showDetail ? detail : "")).trim();
        int ordinal = facesMessage.getSeverity().getOrdinal();
//        ordinal = iconIndex = ++iconIndex % 4;

        writer.startElement("div", messages);
        writer.writeAttribute("class", "ui-corner-all ui-state-" + states[ordinal] + (text.equals("") ? " ui-empty-message" : ""), null);

        writer.startElement("span", messages);
        writer.writeAttribute("class", "ui-icon ui-icon-" + icons[ordinal], null);
        writer.endElement("span");

        if (!text.equals("")) {
            writer.writeText(text, messages, null);
        }
        writer.endElement("div");

        facesMessage.rendered();
    }
}
