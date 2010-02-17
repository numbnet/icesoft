package com.icesoft.faces.component.jseventlistener;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.render.Renderer;

import com.icesoft.faces.component.util.CustomComponentUtils;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;

public class JSEventListenerRenderer extends Renderer{

    public boolean getRendersChildren() {
        return true;
    }
    
    public void decode(FacesContext context, UIComponent uiComponent) {
        Map parameter = context.getExternalContext().getRequestParameterMap();
        if (parameter.containsKey(uiComponent.getClientId(context))) {
            String result = String.valueOf(parameter.get(uiComponent.getClientId(context)));
            if ("submitted".equals(result)) {
                ((JSEventListener)uiComponent).queueEvent(new ActionEvent(uiComponent));
            }
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        writer.startElement(HTML.SCRIPT_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"spt", HTML.ID_ATTR);
        String events = ((JSEventListener)uiComponent).getEvents();
        String handler = ((JSEventListener)uiComponent).getHandler();
        if (handler != null)
            handler = "'"+ handler  + "'";
        if (events != null) {
            writer.write("Ice.registerEventListener('"+ clientId +"','" +
                    events	+"', "+ handler +");");
        }
        
        writer.endElement(HTML.SCRIPT_ELEM);
        

    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HTML.DIV_ELEM);
    }   
    
    public void encodeChildren(FacesContext facesContext,
            UIComponent uiComponent) throws IOException {    
        if (uiComponent.getChildCount() > 0) {
           CustomComponentUtils.renderChildren(facesContext, uiComponent);
        }
    }    
}
