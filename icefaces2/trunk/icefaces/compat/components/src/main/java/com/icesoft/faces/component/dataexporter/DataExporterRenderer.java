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

package com.icesoft.faces.component.dataexporter;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import com.icesoft.faces.renderkit.dom_html_basic.BaseRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.util.CoreUtils;
import com.icesoft.util.pooling.ClientIdPool;

public class DataExporterRenderer extends BaseRenderer {

	public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestMap =
            facesContext.getExternalContext().getRequestParameterMap();
        String clientId = uiComponent.getClientId(facesContext);
        if (requestMap.containsKey("ice.event.captured")) {
            if (clientId.equals(requestMap.get("ice.event.captured"))) {
                uiComponent.queueEvent(new ActionEvent(uiComponent));
            }
	    }
	}
	
	public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
	throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        DataExporter dataExporter = (DataExporter)uiComponent;
        String label = dataExporter.getLabel();
        String image = dataExporter.getImage();
        boolean renderLabelAsButton = dataExporter.isRenderLabelAsButton();
        boolean linkRequired = false;
        String type = dataExporter.getType();
        if ( (type != null &&  !"".equals(type)) ||
                dataExporter.getValueBinding("outputTypeHandler") != null) {
            linkRequired = true;
        }
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, ClientIdPool.get(clientId + "container"), HTML.ID_ATTR);   
        String style = dataExporter.getStyle();
        if (style != null) {
            writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);
        }
        writer.writeAttribute(HTML.CLASS_ATTR, dataExporter.getStyleClass(), HTML.CLASS_ATTR);     
        
        if (linkRequired) {
            if (renderLabelAsButton && image == null) {
                writer.startElement(HTML.INPUT_ELEM, uiComponent);
                writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);        
                writer.writeAttribute(HTML.TYPE_ATTR, HTML.BUTTON_ELEM, HTML.TYPE_ATTR);
                writer.writeAttribute(HTML.ONCLICK_ATTR, "new Ice.DataExporter('" + clientId + "'); return "+ DomBasicRenderer.ICESUBMITPARTIAL, HTML.ONCLICK_ATTR);
                writer.writeAttribute(HTML.VALUE_ATTR, label, HTML.VALUE_ATTR);                
                writer.endElement(HTML.INPUT_ELEM);
            } else {
                writer.startElement(HTML.ANCHOR_ELEM, uiComponent);
                writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);        
                writer.writeAttribute(HTML.HREF_ATTR, "javascript:;", HTML.HREF_ATTR);
                writer.writeAttribute(HTML.ONCLICK_ATTR, "new Ice.DataExporter('" + clientId + "'); var form=formOf(this); return "+ DomBasicRenderer.ICESUBMITPARTIAL, HTML.ONCLICK_ATTR);        
                if (image !=null) {
                    writer.startElement(HTML.IMG_ELEM, uiComponent);
                    writer.writeAttribute(HTML.SRC_ATTR, CoreUtils.resolveResourceURL(facesContext, image), HTML.SRC_ATTR);  
                    writer.writeAttribute(HTML.TITLE_ATTR, label, HTML.TITLE_ATTR);
                    writer.writeAttribute(HTML.ALT_ATTR, label, HTML.ALT_ATTR);                
                    writer.endElement(HTML.IMG_ELEM);
                } else {
                    writer.write(label);
                }
                writer.endElement(HTML.ANCHOR_ELEM);
            }
        }
        writer.endElement(HTML.DIV_ELEM);
	}
}
