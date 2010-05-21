package org.icefaces.component.inputFiles;

import javax.faces.render.Renderer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;

public class InputFilesRenderer extends Renderer {
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        InputFiles inputFiles = (InputFiles) uiComponent;
        String clientId = uiComponent.getClientId(facesContext);
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "file", "type");
        writer.writeAttribute("id", clientId, "clientId");
        writer.writeAttribute("name", clientId, "clientId");
        writer.endElement("input");
    }    
}
