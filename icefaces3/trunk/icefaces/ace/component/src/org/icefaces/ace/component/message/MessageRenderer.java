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

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Iterator;

@MandatoryResourceComponent(tagName = "message", value = "org.icefaces.ace.component.message.Message")
public class MessageRenderer extends Renderer {
//    private static int iconIndex = -1;

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        Message message = (Message) component;
        String forId = message.findComponent(message.getFor()).getClientId();
        Iterator messageIter = context.getMessages(forId);

        writer.startElement("span", message);
        writer.writeAttribute("id", message.getClientId(), "id");
        writer.writeAttribute("class", "ui-faces-message", null);
        if (messageIter.hasNext()) {
            FacesMessage facesMessage = (FacesMessage) messageIter.next();
            if (!facesMessage.isRendered() || message.isRedisplay()) {
                encodeMessage(writer, message, facesMessage);
            }
        }
        writer.endElement("span");
    }

    private void encodeMessage(ResponseWriter writer, Message message, FacesMessage facesMessage) throws IOException {

        String[] states = new String[]{"default", "default", "error", "error"};
        String[] icons = new String[]{"info", "lightbulb", "alert", "flag"};
        boolean showSummary = message.isShowSummary();
        boolean showDetail = message.isShowDetail();
        int ordinal = facesMessage.getSeverity().getOrdinal();
//        ordinal = iconIndex = ++iconIndex % 4;

        writer.startElement("span", message);
        writer.writeAttribute("class", "ui-state-" + states[ordinal], null);

        writer.startElement("span", message);
        writer.writeAttribute("class", "ui-icon ui-icon-" + icons[ordinal], null);
        writer.endElement("span");

        String summary = (null != (summary = facesMessage.getSummary())) ? summary : "";
        String detail = (null != (detail = facesMessage.getDetail())) ? detail : "";
        String text = ((showSummary ? summary : "") + " " + (showDetail ? detail : "")).trim();
        writer.writeText(text, message, null);
        writer.endElement("span");

        facesMessage.rendered();
    }
}
