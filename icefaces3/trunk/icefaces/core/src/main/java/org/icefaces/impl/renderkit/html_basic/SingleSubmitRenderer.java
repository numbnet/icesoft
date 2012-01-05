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
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.impl.renderkit.html_basic;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import javax.faces.render.Renderer;
import java.io.IOException;

public class SingleSubmitRenderer extends Renderer {
    public static String SINGLE_SUBMIT_MARKER = SingleSubmitRenderer.class.getName();

    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        UIComponent parent = component.getParent();
        String parentId = parent.getClientId();
        parent.getAttributes().put(SINGLE_SUBMIT_MARKER, SINGLE_SUBMIT_MARKER);
        String submitOnBlur = "false";
        if ( "true".equalsIgnoreCase( (String)
                component.getAttributes().get("submitOnBlur")) )  {
            submitOnBlur = "true";
        }
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", component);
        writer.writeAttribute("id", component.getClientId(), "id");
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", "type");
        writer.writeText("ice.enableSingleSubmit('" + parentId + "'," +
                submitOnBlur + ");", component, null);
        writer.endElement("script");
        writer.endElement("div");
    }

}
