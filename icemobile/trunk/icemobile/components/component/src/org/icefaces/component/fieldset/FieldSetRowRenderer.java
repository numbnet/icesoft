/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icefaces.component.fieldset;


import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.Utils;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.render.Renderer;

import java.io.IOException;

import java.util.logging.Logger;


public class FieldSetRowRenderer extends Renderer {
    private static Logger logger = Logger.getLogger(FieldSetRowRenderer.class.getName());

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        FieldSetRow field = (FieldSetRow) uiComponent;

         //no javascript tag for this component
        //do I have to make sure that the items only have a parent that is an OutputList?
        
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        String userDefinedClass = field.getStyleClass();
        String styleClass = FieldSetRow.FIELDSETROW_CLASS;
        if (field.isGroup()){
        	styleClass = FieldSetRow.FIELDSETGROUP_CLASS;
        	if (userDefinedClass!=null){
        	    styleClass += " "+userDefinedClass;
        	}
        }   
        writer.writeAttribute("class", styleClass, "styleClass");
  
     }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        writer.endElement(HTML.DIV_ELEM);
    }
}
