package org.icefaces.ace.component.gmap;


import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class GMapServices extends GMapServicesBase {

    public void encodeBegin(FacesContext context, GMapServices gLayer) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = getClientId(context);
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("ice.ace.jq(function() {");
        if (getPoints() != null && getName() != null){
            writer.write("ice.ace.gMap.gService('" + this.getParent().getClientId(context) + "' , '" + getName() + "' , '" + getPoints() + "' , \"" + getOptions() + "\");");
        }
        writer.write("});");
        writer.endElement("script");
    }

}