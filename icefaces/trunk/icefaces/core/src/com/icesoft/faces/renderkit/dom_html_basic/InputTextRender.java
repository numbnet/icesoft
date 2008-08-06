package com.icesoft.faces.renderkit.dom_html_basic;

import com.icesoft.faces.component.AttributeConstants;
import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;


public class InputTextRender extends BaseRenderer{
    
    private static final String[] passThruAttributes = AttributeConstants.getAttributes(AttributeConstants.H_INPUTTEXT);

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        //readonly or disabled components are not required to submit the value
        if(DomBasicRenderer.isStatic(uiComponent)) return;
        
        String clientId = uiComponent.getClientId(facesContext);
        Map requestMap =
                facesContext.getExternalContext().getRequestParameterMap();
        if (requestMap.containsKey(clientId)) {
            String decodedValue = (String) requestMap.get(clientId);
            ((UIInput)uiComponent).setSubmittedValue(decodedValue);
        }
    }
    
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        
        PassThruAttributeWriter.renderHtmlAttributes(writer, uiComponent, passThruAttributes);

        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);              
        Object styleClass = uiComponent.getAttributes().get("styleClass");
        if (styleClass != null) {
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
        } 
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        //it must call the super.encode to support effects and facesMessage recovery
        super.encodeEnd(facesContext, uiComponent);
        ResponseWriter writer = facesContext.getResponseWriter();
        Object value = getValue(facesContext, uiComponent);
        writer.writeAttribute(HTML.VALUE_ATTR, value, null);
        writer.endElement(HTML.INPUT_ELEM);
    }
    
    public String getValue(FacesContext facesContext, UIComponent uiComponent) {
        // for input components, get the submitted value
        if (uiComponent instanceof UIInput) {
            Object submittedValue = ((UIInput) uiComponent).getSubmittedValue();
            if (submittedValue != null && submittedValue instanceof String) {
                return (String) submittedValue;
            }
        }
        return DomBasicInputRenderer.converterGetAsString(facesContext, 
                uiComponent, ((UIInput) uiComponent).getValue());
    }
    
}