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

import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.util.EnvUtils;

import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@MandatoryResourceComponent(tagName = "messages", value = "org.icefaces.ace.component.messages.Messages")
public class MessagesRenderer extends Renderer {

    private static final String[] icons = new String[]{"info", "notice", "alert", "alert"};
    private static final String[] states = new String[]{"highlight", "highlight", "error", "error"};
    private static final Set<String> effectSet = new HashSet<String>(Arrays.asList("blind", "bounce", "clip", "drop", "explode", "fade", "fold", "highlight", "puff", "pulsate", "scale", "shake", "size", "slide"));
    private static final Set<String> durationSet = new HashSet<String>(Arrays.asList("slow", "_default", "fast"));
    private static final Logger logger = Logger.getLogger(MessagesRenderer.class.getName());

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        Messages messages = (Messages) component;
        String forId = (forId = messages.getFor()) == null ? "@all" : forId.trim();
        Iterator messageIter;
        String style = messages.getStyle();
        String styleClass = (styleClass = messages.getStyleClass()) == null ? "" : " " + styleClass;
        boolean ariaEnabled = EnvUtils.isAriaEnabled(context);
        String sourceMethod = "encodeEnd";

/*
        if (forId.equals("@all")) {
            messageIter = messages.isGlobalOnly() ? context.getMessages(null) : context.getMessages();
        } else {
            UIComponent forComponent = forId.equals("") ? null : messages.findComponent(forId);
            if (forComponent == null) {
                logger.logp(Level.WARNING, logger.getName(), sourceMethod, "'for' attribute value cannot be empty or non-existent id.");
                return;
            }
            messageIter = context.getMessages(forComponent.getClientId(context));
        }
*/
        messageIter = messages.getMsgList(context);
        writer.startElement("div", messages);
        String clientId = messages.getClientId();
        writer.writeAttribute("id", clientId, "id");
        ComponentUtils.enableOnElementUpdateNotify(writer, clientId);
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }
        writer.writeAttribute("class", "ui-faces-messages ui-widget" + styleClass, null);
        if (ariaEnabled) {
            writer.writeAttribute("role", "alert", null);
            writer.writeAttribute("aria-atomic", "true", null);
            writer.writeAttribute("aria-live", "polite", null);
            writer.writeAttribute("aria-relevant", "all", null);
        }
        Set<String> msgSet = new HashSet<String>();
        int count = 1;
        while (messageIter.hasNext()) {
            Messages.AceFacesMessage aceFacesMessage = (Messages.AceFacesMessage) messageIter.next();
            FacesMessage facesMessage = aceFacesMessage.getFacesMessage();
            if (!facesMessage.isRendered() || messages.isRedisplay()) {
                encodeMessage(writer, messages, facesMessage, aceFacesMessage.getClientId(), msgSet, count++);
            }
        }
        writer.endElement("div");
        messages.setPrevMsgSet(msgSet);
    }

    private void encodeMessage(ResponseWriter writer, Messages messages, FacesMessage facesMessage, String clientId, Set<String> msgSet, int count) throws IOException {

        String sourceMethod = "encodeMessage";
        clientId = clientId != null ? clientId : "global";
        boolean showSummary = messages.isShowSummary();
        boolean showDetail = messages.isShowDetail();
        String summary = (null != (summary = facesMessage.getSummary())) ? summary : "";
        String detail = (null != (detail = facesMessage.getDetail())) ? detail : ""; // Mojarra defaults to summary. Not good.
        String text = ((showSummary ? summary : "") + " " + (showDetail ? detail : "")).trim();
        int ordinal = (ordinal = FacesMessage.VALUES.indexOf(facesMessage.getSeverity())) > -1 && ordinal < states.length ? ordinal : 0;

        writer.startElement("div", messages);
        writer.writeAttribute("id", clientId + "_msg" + count, "id");
        writer.writeAttribute("class", "ui-corner-all ui-state-" + states[ordinal] + (text.equals("") ? " ui-empty-message" : ""), null);
        writeAttributes(writer, messages, "lang", "title");

        writer.startElement("span", messages);
        writer.writeAttribute("class", "ui-icon ui-icon-" + icons[ordinal], null);
        writer.endElement("span");

        String currClientIdPlusText = clientId + ":" + text;
        if (!text.equals("")) {
            if (messages.isEscape()) {
                writer.writeText(text, messages, null);
            } else {
                writer.write(text);
            }
            if (!msgSet.contains(currClientIdPlusText)) {
                msgSet.add(currClientIdPlusText);
            }
        }
        writer.endElement("div");

        String event = "", effect = "", duration = "";
        Set preMsgSet = (preMsgSet = messages.getPrevMsgSet()) != null ? preMsgSet : Collections.emptySet();
        System.out.println("currClientIdPlusText = " + currClientIdPlusText);
        for (Object msg : preMsgSet) {
            System.out.println("msg = " + msg);
        }
        if (!preMsgSet.contains(currClientIdPlusText)) {
            event = "init";
            effect = (effect = messages.getInitEffect()) != null ? effect.trim() : "";
            logInvalid(effectSet, "effect", effect, sourceMethod);
            effect = effectSet.contains(effect) ? effect : "";
            duration = (duration = messages.getInitEffectDuration()) != null ? duration.trim() : "";
/*
        } else if (!prevText.equals("") && !currText.equals("") && !prevText.equals(currText)) {
            event = "change";
            effect = (effect = messages.getChangeEffect()) != null ? effect.trim() : "";
            logInvalid(effectSet, "effect", effect, sourceMethod);
            effect = effectSet.contains(effect) ? effect : "";
            duration = (duration = messages.getChangeEffectDuration()) != null ? duration.trim() : "";
*/
        }
        if (!(event.equals("") || effect.equals(""))) {
            JSONBuilder jb = JSONBuilder.create();
            jb.beginMap()
                    .entry("id", clientId)
                    .entry("count", count)
                    .entry("event", event)
                    .entry("effect", effect);
            try {
                jb.entry("duration", Integer.parseInt(duration));
            } catch (NumberFormatException e) {
                logInvalid(durationSet, "duration", duration, sourceMethod);
                duration = durationSet.contains(duration) ? duration : "_default";
                jb.entry("duration", duration);
            }
            jb.endMap();
            writer.startElement("script", null);
            writer.write("ice.ace.Message.factory(" + jb + ");//" + UUID.randomUUID());
            writer.endElement("script");
        }

        facesMessage.rendered();
    }

    private void writeAttributes(ResponseWriter writer, UIComponent component, String... keys) throws IOException {
        Object value;
        for (String key : keys) {
            value = component.getAttributes().get(key);
            if (value != null) {
                writer.writeAttribute(key, value, key);
            }
        }
    }

    private void log(Level level, String sourceMethod, String message) {
        if (!FacesContext.getCurrentInstance().isProjectStage(ProjectStage.Development)) return;
        logger.logp(level, logger.getName(), sourceMethod, message);
    }

    private void logInvalid(Set<String> validSet, String name, String value, String sourceMethod) {
        if (!value.equals("") && !validSet.contains(value)) {
            log(Level.WARNING, sourceMethod, "Invalid " + name + " \"" + value + "\" reset to default. Read TLD doc.");
        }
    }
}
