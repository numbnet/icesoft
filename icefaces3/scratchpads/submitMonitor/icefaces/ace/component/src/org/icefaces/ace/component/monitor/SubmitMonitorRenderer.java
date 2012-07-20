package org.icefaces.ace.component.monitor;

import org.icefaces.ace.component.chart.Chart;
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
        writer.write("var " + resolveWidgetVar(monitor) + " = ice.ace.Monitor(" + getConfig(monitor) + ");");
        writer.endElement(HTML.SCRIPT_ELEM);

        writer.endElement(HTML.DIV_ELEM);
    }

    private void writeComponent(SubmitMonitor monitor, ResponseWriter writer, String clientId) throws IOException {
        String label = monitor.getActiveLabel();
        String styleClass = "if-sub-mon";

        if (monitor.isCentered()) styleClass += " center";

        writer.startElement(HTML.DIV_ELEM, monitor);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_display", null);
        writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
        if (Boolean.valueOf(true).equals(monitor.isBlockUI()))
            writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", null);

        writer.startElement(HTML.IMG_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "if-sub-mon-img", null);
        writer.endElement(HTML.IMG_ELEM);

        if (label != null) {
            writer.startElement(HTML.SPAN_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, "if-sub-mon-txt", null);
            writer.write(label);
            writer.endElement(HTML.SPAN_ELEM);
        }

        writer.endElement(HTML.DIV_ELEM);
    }

    public JSONBuilder getConfig(SubmitMonitor monitor) {
        JSONBuilder config = new JSONBuilder();
        FacesContext context = FacesContext.getCurrentInstance();

        Boolean blockUI = monitor.isBlockUI();

        String activeLabel = monitor.getActiveLabel();
        String idleLabel = monitor.getIdleLabel();
        String serverErrorLabel = monitor.getServerErrorLabel();
        String networkErrorLabel = monitor.getNetworkErrorLabel();
        String sessionExpiredLabel = monitor.getSessionExpiredLabel();

        String activeImgUrl = getResourceRequestPath(context, "monitor/connect_active.gif");
        String cautionImgUrl = getResourceRequestPath(context, "monitor/connect_caution.gif");
        String disconnectedImgUrl = getResourceRequestPath(context, "monitor/connect_disconnected.gif");
        String idleImgUrl = getResourceRequestPath(context, "monitor/connect_idle.gif");

        config.beginMap();
        config.entry("id", monitor.getClientId());

        if (blockUI != null) config.entry("blockUI", blockUI);


        if (idleLabel != null) config.entry("idleLabel", idleLabel);
        if (activeLabel != null) config.entry("activeLabel", activeLabel);
        if (networkErrorLabel != null) config.entry("networkErrorLabel", networkErrorLabel);
        if (serverErrorLabel != null) config.entry("serverErrorLabel", serverErrorLabel);
        if (sessionExpiredLabel != null) config.entry("sessionExpiredLabel", sessionExpiredLabel);

        if (activeImgUrl != null) config.entry("activeImgUrl", activeImgUrl);
        if (cautionImgUrl != null) config.entry("cautionImgUrl", cautionImgUrl);
        if (disconnectedImgUrl != null) config.entry("disconnectedImgUrl", disconnectedImgUrl);
        if (idleImgUrl != null) config.entry("idleImgUrl", idleImgUrl);


        config.endMap();

        return config;
    }
}
