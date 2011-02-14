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

package org.icefaces.component.logger;

import java.io.IOException;
import java.util.*;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;


import org.icefaces.component.utils.HTML;
import org.icefaces.render.MandatoryResourceComponent;


//@MandatoryResourceComponent("org.icefaces.component.logger.Logger")
public class LoggerRenderer extends Renderer {

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.containsKey("ice.event.captured")) {
//don't need to do anything..unless we want to put a button on there to capture 
// the log file 
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
    	
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Logger logger = (Logger) uiComponent;
	
		// root element
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
		writer.writeAttribute(HTML.CLASS_ATTR, "yui-log", null);
		
		// first child
//		writer.startElement(HTML.SPAN_ELEM, uiComponent);
//		writer.writeAttribute(HTML.CLASS_ATTR, "first-child", null);
//		writer.startElement(HTML.SPAN_ELEM, uiComponent);
//		writer.write(logger.getLabel());
//		writer.endElement(HTML.SPAN_ELEM);
		
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
		String clientId = uiComponent.getClientId(facesContext);
		Logger logger = (Logger) uiComponent;
		writer.endElement(HTML.SPAN_ELEM);
//		writer.endElement(HTML.SPAN_ELEM);
		
		// js call
        StringBuilder call= new StringBuilder();
        call.append("ice.component.logger.updateProperties('");
        call.append(clientId);
        call.append("', ");
        //pass through YUI  properties 
        call.append("{");
//        call.append("label:'");
//        call.append(logger.getLabel());
//        call.append(", ");  
        call.append("debugElement:'");
        call.append(logger.getDebugElement());
        call.append("'");         

        call.append("},");
        //pass JSF component specific properties that would help in slider configuration 
        call.append("{");    
        call.append("tabindex:");
        call.append(logger.getTabindex());   
        call.append("});");
 
        writer.startElement(HTML.SCRIPT_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "script", HTML.ID_ATTR);             
        writer.write(call.toString());
        writer.endElement(HTML.SCRIPT_ELEM);
    }
}
