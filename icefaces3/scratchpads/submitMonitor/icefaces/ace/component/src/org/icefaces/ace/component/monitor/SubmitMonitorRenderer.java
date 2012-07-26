package org.icefaces.ace.component.monitor;

import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <p/>
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * User: Nils
 * Date: 12-07-17
 * Time: 3:15 PM
 */
public class SubmitMonitorRenderer extends CoreRenderer {

    @Override
    public void	encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId();
        SubmitMonitor monitor = (SubmitMonitor)component;

        // Encode Component
        writeComponent(monitor, writer, clientId);

        //Encode Script
        writeScript(monitor, writer, clientId);
    }

    private void writeScript(SubmitMonitor monitor, ResponseWriter writer, String clientId) throws IOException {
        writer.startElement(HTML.DIV_ELEM, monitor);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_script", null);

        writer.startElement(HTML.SCRIPT_ELEM, null);
        JSONBuilder json = JSONBuilder.create();
        json.initialiseVar(resolveWidgetVar(monitor));
        json.beginFunction("ice.ace.Monitor");
        getConfig(monitor, json);
        json.endFunction();
        writer.write(json.toString());
        writer.endElement(HTML.SCRIPT_ELEM);

        writer.endElement(HTML.DIV_ELEM);
    }

    private void writeComponent(SubmitMonitor monitor, ResponseWriter writer, String clientId) throws IOException {
        writer.startElement(HTML.DIV_ELEM, monitor);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_display", null);
        writer.writeAttribute(HTML.CLASS_ATTR, "if-sub-mon", null);
        if (Boolean.TRUE.equals(monitor.isHidingIdleSubmitMonitor())) {
            writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", null);
        }

        writer.startElement(HTML.DIV_ELEM, monitor);
        writer.writeAttribute(HTML.CLASS_ATTR, "if-sub-mon-mid", null);

        writer.startElement(HTML.IMG_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "if-sub-mon-img", null);
        writer.endElement(HTML.IMG_ELEM);

        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "if-sub-mon-txt", null);
        String idleLabel = monitor.getIdleLabel();
        if (idleLabel != null && idleLabel.length() > 0) {
            writer.write(idleLabel);
        }
        writer.endElement(HTML.SPAN_ELEM);

        writer.endElement(HTML.DIV_ELEM);

        writer.endElement(HTML.DIV_ELEM);
    }

    public JSONBuilder getConfig(SubmitMonitor monitor, JSONBuilder config) {
        FacesContext context = FacesContext.getCurrentInstance();

        config.beginMap();
        config.entry("id", monitor.getClientId());

        config.entryNonNullValue("blockUI", monitor.resolveBlockUI());
        config.entryNonNullValue("autoCenter", monitor.isAutoCenter());

        config.entryNonNullValue("idleLabel", monitor.getIdleLabel());
        config.entryNonNullValue("activeLabel", monitor.getActiveLabel());
        config.entryNonNullValue("networkErrorLabel", monitor.getNetworkErrorLabel());
        config.entryNonNullValue("serverErrorLabel", monitor.getServerErrorLabel());
        config.entryNonNullValue("sessionExpiredLabel", monitor.getSessionExpiredLabel());

        config.entryNonNullValue("idleImgUrl", resolveImage(context,
            null/*TODO monitor.getIdleImage()*/, "monitor/connect_idle.gif"));
        config.entryNonNullValue("activeImgUrl", resolveImage(context,
            null/*TODO monitor.getActiveImage()*/, "monitor/connect_active.gif"));
        config.entryNonNullValue("networkErrorImgUrl", resolveImage(context,
            null/*TODO monitor.getNetworkErrorImage()*/, "monitor/connect_disconnected.gif"));
        config.entryNonNullValue("serverErrorImgUrl", resolveImage(context,
            null/*TODO monitor.getServerErrorImage()*/, "monitor/connect_disconnected.gif"));
        config.entryNonNullValue("sessionExpiredImgUrl", resolveImage(context,
            null/*TODO monitor.getSessionExpiredImage()*/, "monitor/connect_session.gif"));

        config.endMap();
        return config;
    }

    protected String resolveImage(FacesContext context, String propertyValue, String builtInImage) {
        //TODO If the property was set, use it, since they could set it to empty to disable the image
        if (propertyValue != null && propertyValue.length() > 0) {
            return propertyValue;
        }
        return getResourceRequestPath(context, builtInImage);
    }
}
