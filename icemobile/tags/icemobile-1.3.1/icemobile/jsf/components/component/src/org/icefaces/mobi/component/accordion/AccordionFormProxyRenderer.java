/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
package org.icefaces.mobi.component.accordion;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

public class AccordionFormProxyRenderer extends Renderer {

    private static final Logger logger = Logger.getLogger(AccordionFormProxyRenderer.class.getName());
    
    private Accordion findTargetAccordion(FacesContext context, AccordionFormProxy proxy){
        Accordion accordion = findParentAccordion(proxy);
        if( accordion == null ){
            String accordionClientId = proxy.getFor();
            if( accordionClientId != null ){
                accordion = (Accordion)context.getViewRoot().findComponent(accordionClientId);
            }
        }
        return accordion;
    }
    
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        AccordionFormProxy proxy = (AccordionFormProxy)uiComponent;
        Accordion accordion = findTargetAccordion(facesContext, proxy);
        if( accordion == null ){
            logger.warning("could not locate parent Accordion or the 'for' attribute, exiting");
            return;
        }
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        writer.startElement("span", null);
        writer.writeAttribute("id", clientId, null);
        writer.startElement("input", null);
        writer.writeAttribute("id", clientId+"_hidden", null);
        writer.writeAttribute("type", "hidden", null);
        String proxyFor = proxy.getFor();
        if( proxyFor != null && proxyFor.length() > 0 ){
            writer.writeAttribute("data-for", proxy.getFor(), null);
        }
        
        writer.writeAttribute("class", "mobi-accordion-proxy", null);
        writer.writeAttribute("name", accordion.getClientId() + "_hidden", null);
        String currentId = accordion.getSelectedId();
        if( currentId != null && currentId.length() > 0 ){
             writer.writeAttribute("value", currentId, null);
        }
        writer.endElement("input");
        writer.endElement("span");
    }
    
    private Accordion findParentAccordion(UIComponent uic){
        Accordion accordion = null;
        UIComponent parent = uic.getParent();
        while( parent != null ){
            if( parent instanceof Accordion ){
                accordion = (Accordion)parent;
                break;
            }
            parent = parent.getParent();
        }
        return accordion;
    }
    

    public boolean getRendersChildren() {
        return true;
    }

    
}
