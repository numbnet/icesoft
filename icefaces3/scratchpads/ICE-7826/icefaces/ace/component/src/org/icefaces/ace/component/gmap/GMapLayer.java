package org.icefaces.ace.component.gmap;


import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class GMapLayer extends GMapLayerBase {

    public void encodeBegin(FacesContext context, GMapLayer gLayer) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = getClientId(context);
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("ice.ace.jq(function() {");
        if (getLayerType() != null){
            writer.write("ice.ace.gMap.removeMapLayer('" + this.getParent().getClientId(context) + "', '" + clientId +"');");
            if(getUrl() != null)
                writer.write("ice.ace.gMap.addMapLayer('" + this.getParent().getClientId(context) + "', '" + clientId +
                    "', '"+ getLayerType() + "', \"" + getOptions() + "\", '" + getUrl() + "');");
            else
                writer.write("ice.ace.gMap.addMapLayer('" + this.getParent().getClientId(context) + "', '" + clientId +
                   "', '"+ getLayerType() + "', \"" + getOptions() + "\");");
        }
        writer.write("});");
        writer.endElement("script");
    }

}