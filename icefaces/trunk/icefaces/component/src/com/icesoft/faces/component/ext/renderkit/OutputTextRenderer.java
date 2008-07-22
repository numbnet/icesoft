package com.icesoft.faces.component.ext.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.icesoft.faces.component.ext.HtmlOutputText;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicInputRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.util.DOMUtils;

public class OutputTextRenderer extends Renderer{
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        UIOutput component = (UIOutput) uiComponent;
        ResponseWriter writer = facesContext.getResponseWriter();
        Object rawValue = component.getValue(); 
        //This is not ice:outputText, so just render the value
        //this will be true for open HTML in the JSP
        if (!(component instanceof HtmlOutputText)) {
            writer.write(String.valueOf(rawValue));
            return;
        }
        
        //All ice components has default styleClass, so there is not need
        //to check if the span was required
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, uiComponent.getClientId(facesContext), 
                                                null);
        String convertedValue = null;
        convertedValue = DomBasicInputRenderer.converterGetAsString(facesContext, 
                                                        uiComponent, rawValue);
        boolean valueTextRequiresEscape = DOMUtils.escapeIsRequired(uiComponent);
        if (valueTextRequiresEscape) {
            convertedValue = DOMUtils.escapeAnsi(convertedValue);
        } 
        writer.write(convertedValue);
        renderPassThruAttributes(writer, uiComponent);
        Object styleClass = uiComponent.getAttributes().get("styleClass");
        if (styleClass != null) {
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
        }        
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        if (!(uiComponent instanceof HtmlOutputText)) {
            return;
        }
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HTML.SPAN_ELEM);
    }
    
    private void renderPassThruAttributes(ResponseWriter writer, UIComponent uiComponent) throws IOException{
        String[] attributes = (String[]) uiComponent.getAttributes().get("supportedPassThru");
        if (attributes == null) return;
        for (int i=0; i < attributes.length; i++) {
            Object value = null;
            if ((value = uiComponent.getAttributes().get(attributes[i])) != null) {
                writer.writeAttribute(attributes[i], value, null);
            }
        }
    }
}
