package com.icesoft.faces.renderkit.dom_html_basic;

import com.icesoft.faces.component.AttributeConstants;
import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.icesoft.faces.context.effects.LocalEffectEncoder;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicInputRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.util.DOMUtils;

import com.icesoft.util.pooling.ClientIdPool;

public class OutputTextRenderer extends BaseRenderer{
    
    private static final String[] passThruAttributes = AttributeConstants.getAttributes(AttributeConstants.H_OUTPUTTEXT);
    
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        UIOutput component = (UIOutput) uiComponent;
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = ClientIdPool.get(uiComponent.getClientId(facesContext));
        //This is not ice:outputText, so just render the value
        //this will be true for open HTML in the JSP
        if (!(component instanceof HtmlOutputText)) {
            Object value = component.getValue();
            if (value != null) {
                String svalue = String.valueOf(value);
                if (svalue.length() > 0) {
                    writer.write(svalue);
                }
            }
            return;
        }

        Boolean nospan = (Boolean) uiComponent.getAttributes().get("nospan");
        if (nospan != null && nospan.booleanValue()) return;
        Object styleClass = uiComponent.getAttributes().get("styleClass");
        //could be true only for the h:outputText
        if (styleClass == null)return;
        
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        PassThruAttributeWriter.renderHtmlAttributes(writer, uiComponent, passThruAttributes);

        if (styleClass != null) {
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
        }   
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        if (!(uiComponent instanceof HtmlOutputText)) {
            return;
        }
        //it must call the super.encode to support effects and facesMessage recovery
        super.encodeEnd(facesContext, uiComponent); 
        ResponseWriter writer = facesContext.getResponseWriter();
        UIOutput component = (UIOutput) uiComponent;
        Object rawValue = component.getValue();         
        String convertedValue = DomBasicInputRenderer.converterGetAsString(
            facesContext, uiComponent, rawValue);
        boolean valueTextRequiresEscape = DOMUtils.escapeIsRequired(uiComponent);
        if (valueTextRequiresEscape) {
            convertedValue = DOMUtils.escapeAnsi(convertedValue);
        } 
        writer.write(convertedValue);

        Boolean nospan = (Boolean) uiComponent.getAttributes().get("nospan");
        if (nospan != null && nospan.booleanValue()) return;
        Object styleClass = uiComponent.getAttributes().get("styleClass");
        //could be true only for the h:outputText
        if (styleClass == null)return;
        
        LocalEffectEncoder.encodeLocalEffects(uiComponent, writer, facesContext);        
        writer.endElement(HTML.SPAN_ELEM);
    }
}
