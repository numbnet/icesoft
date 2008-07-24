package com.icesoft.faces.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

import com.icesoft.faces.renderkit.dom_html_basic.HTML;

public class RendererUtil {

    public static String SUPPORTED_PASSTHRU_ATT = "supportedPassThru";
    public static void renderPassThruAttributes(ResponseWriter writer, UIComponent uiComponent) throws IOException{
        String[] attributes = (String[]) uiComponent.getAttributes().get(SUPPORTED_PASSTHRU_ATT);
        if (attributes == null) return;
        for (int i=0; i < attributes.length; i++) {
            Object value = null;
            if ((value = uiComponent.getAttributes().get(attributes[i])) != null) {
                writer.writeAttribute(attributes[i], value, null);
            }
        }
    } 
    
    //This is to just put necessary things together required for the root element.
    public static void writeRootElement(ResponseWriter writer, 
                                    UIComponent uiComponent,
                                    String clientId,
                                    String element) throws IOException{
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);  
        RendererUtil.renderPassThruAttributes(writer, uiComponent);
    }
}
