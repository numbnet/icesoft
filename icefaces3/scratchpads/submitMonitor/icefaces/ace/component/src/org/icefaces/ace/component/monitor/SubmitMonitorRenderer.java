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

        writer.startElement(HTML.DIV_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);

        writer.startElement(HTML.SCRIPT_ELEM, null);
        writer.write("var " + resolveWidgetVar(component) + " = ice.ace.Monitor(" + getConfig(monitor) + ");");
        writer.endElement(HTML.SCRIPT_ELEM);

        writer.endElement(HTML.DIV_ELEM);
    }

    public JSONBuilder getConfig(SubmitMonitor monitor) {
        JSONBuilder config = new JSONBuilder();
        String label = monitor.getLabel();

        config.beginMap();
        if (label != null) config.entry("label", label);
        config.endMap();

        return config;
    }
}
