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

package org.icefaces.ace.component.submitmonitor;

import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmitMonitorRenderer extends CoreRenderer {
    //TODO Replace with background image styling
    private static Map<String, String> FACET_NAME_TO_IMG_SRC;
    static {
        FACET_NAME_TO_IMG_SRC = new HashMap<String, String>(5);
        FACET_NAME_TO_IMG_SRC.put("idle", "submitmonitor/connect_idle.gif");
        FACET_NAME_TO_IMG_SRC.put("active", "submitmonitor/connect_active.gif");
        FACET_NAME_TO_IMG_SRC.put("serverError", "submitmonitor/connect_disconnected.gif");
        FACET_NAME_TO_IMG_SRC.put("networkError", "submitmonitor/connect_disconnected.gif");
        FACET_NAME_TO_IMG_SRC.put("sessionExpired", "submitmonitor/connect_session.gif");
    }

    @Override
    public void	encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId();
        SubmitMonitor monitor = (SubmitMonitor)component;

        // Encode Component
        writeComponent(context, writer, monitor, clientId);

        //Encode Script
        writeScript(context, writer, monitor, clientId);
    }

    private void writeScript(FacesContext context, ResponseWriter writer,
            SubmitMonitor monitor, String clientId) throws IOException {
        writer.startElement(HTML.DIV_ELEM, monitor);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_script", null);

        writer.startElement(HTML.SCRIPT_ELEM, null);
        JSONBuilder json = JSONBuilder.create();
        json.initialiseVar(resolveWidgetVar(monitor));
        json.beginFunction("ice.ace.SubmitMonitor");
        writeConfig(monitor, json);
        json.endFunction();
        writer.write(json.toString());
        writer.endElement(HTML.SCRIPT_ELEM);

        writer.endElement(HTML.DIV_ELEM);
    }

    private void writeComponent(FacesContext context, ResponseWriter writer,
            SubmitMonitor monitor, String clientId) throws IOException {
        writer.startElement(HTML.DIV_ELEM, monitor);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_display", null);
        writer.writeAttribute(HTML.CLASS_ATTR, "if-sub-mon", null);
        if (Boolean.TRUE.equals(monitor.isHidingIdleSubmitMonitor())) {
            writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", null);
        }

        // Don't give anything under here an id, since this section is cloned,
        // and we don't want duplicate id(s) in the DOM. It may be problematic
        // with the facet components having id(s).

        final List<String> allFacets = Arrays.asList(new String[] {
            "idle", "active", "serverError", "networkError", "sessionExpired"
        });
        for (String facetName : allFacets) {
            writer.startElement(HTML.DIV_ELEM, monitor);
            // Start us off in the idle state. When first rendering, this
            // puts us in the right state, since we don't have any listeners
            // setup to transition us from active to idle. But on subsequent
            // renders, if the submitMonitor itself is updated, then things
            // could get dicey. But it's probably best to be back in idle
            // anyway.
            if (!facetName.equals(allFacets.get(0))) {
                writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", null);
            }
            writer.writeAttribute(HTML.CLASS_ATTR, "if-sub-mon-mid " + facetName, null);

            UIComponent facet = monitor.getFacet(facetName);
            if (facet != null) {
                facet.encodeAll(context);
            } else {
                writer.startElement(HTML.IMG_ELEM, null);
                writer.writeAttribute(HTML.CLASS_ATTR, "if-sub-mon-img", null);
                writer.writeAttribute(HTML.SRC_ATTR,  getResourceRequestPath(
                    context, FACET_NAME_TO_IMG_SRC.get(facetName)), null);
                writer.endElement(HTML.IMG_ELEM);

                writer.startElement(HTML.SPAN_ELEM, null);
                writer.writeAttribute(HTML.CLASS_ATTR, "if-sub-mon-txt", null);
                String label;
                switch (allFacets.indexOf(facetName)) {
                    case 1: label = monitor.getActiveLabel(); break;
                    case 2: label = monitor.getServerErrorLabel(); break;
                    case 3: label = monitor.getNetworkErrorLabel(); break;
                    case 4: label = monitor.getSessionExpiredLabel(); break;
                    case 0: default: label = monitor.getIdleLabel(); break;
                }
                if (label != null && label.length() > 0) {
                    writer.write(label);
                }
                writer.endElement(HTML.SPAN_ELEM);
            }
            writer.endElement(HTML.DIV_ELEM);
        }

        writer.endElement(HTML.DIV_ELEM);
    }

    public JSONBuilder writeConfig(SubmitMonitor monitor, JSONBuilder config) {
        config.beginMap();
        config.entry("id", monitor.getClientId());
        config.entryNonNullValue("blockUI", monitor.resolveBlockUI());
        config.entryNonNullValue("autoCenter", monitor.isAutoCenter());
        config.endMap();
        return config;
    }
}
