package org.icefaces.ace.component.gmap;

import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.render.MandatoryResourceComponent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

@MandatoryResourceComponent(tagName="gMap", value="org.icefaces.ace.component.gmap.GMap")
public class GMapServicesRenderer extends CoreRenderer {


    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {

        GMapServices service = (GMapServices) component;
        service.encodeBegin(context, service);
    }
}