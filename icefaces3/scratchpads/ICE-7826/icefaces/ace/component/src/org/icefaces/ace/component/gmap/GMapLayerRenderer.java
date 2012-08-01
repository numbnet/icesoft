package org.icefaces.ace.component.gmap;

import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.render.MandatoryResourceComponent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

@MandatoryResourceComponent(tagName="gMap", value="org.icefaces.ace.component.gmap.GMap")
public class GMapLayerRenderer extends CoreRenderer {


    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {

        GMapLayer layer = (GMapLayer) component;
        layer.encodeBegin(context, layer);
    }
}