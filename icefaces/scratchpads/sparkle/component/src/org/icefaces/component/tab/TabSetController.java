package org.icefaces.component.tab;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.component.utils.HTML;

public class TabSetController extends TabSetControllerBase{

	
    public void encodeBegin(FacesContext context) throws IOException {
    	super.encodeBegin(context);
    	ResponseWriter writer = context.getResponseWriter();
    	String id = getFor() + "_tsc";
    	writer.startElement(HTML.INPUT_ELEM, this); 	
    	writer.writeAttribute(HTML.ID_ATTR, id, HTML.ID_ATTR);
    	writer.endElement(HTML.INPUT_ELEM);
    }
}
