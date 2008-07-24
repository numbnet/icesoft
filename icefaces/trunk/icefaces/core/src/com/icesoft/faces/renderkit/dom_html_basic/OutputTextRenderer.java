package com.icesoft.faces.renderkit.dom_html_basic;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.icesoft.faces.renderkit.RendererUtil;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicInputRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.util.DOMUtils;

public class OutputTextRenderer extends BaseRenderer{
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        UIOutput component = (UIOutput) uiComponent;
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Object rawValue = component.getValue(); 
        //This is not ice:outputText, so just render the value
        //this will be true for open HTML in the JSP
        if (!(component instanceof HtmlOutputText)) {
            writer.write(String.valueOf(rawValue));
            return;
        }
        
        writeRootElement(writer, uiComponent, clientId, HTML.SPAN_ELEM);
        String convertedValue = null;
        convertedValue = DomBasicInputRenderer.converterGetAsString(facesContext, 
                                                        uiComponent, rawValue);
        boolean valueTextRequiresEscape = DOMUtils.escapeIsRequired(uiComponent);
        if (valueTextRequiresEscape) {
            convertedValue = DOMUtils.escapeAnsi(convertedValue);
        } 
        writer.write(convertedValue);
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
}
