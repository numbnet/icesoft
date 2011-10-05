/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
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
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", component);
        writer.writeAttribute("id", component.getClientId(), "id");
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", "type");
        writer.writeText("ice.enableSingleSubmit('" + parentId + "');", component, null);
        writer.endElement("script");
        writer.endElement("div");
    }

}
