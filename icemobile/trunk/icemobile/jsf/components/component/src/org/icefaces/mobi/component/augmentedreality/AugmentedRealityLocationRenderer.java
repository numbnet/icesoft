package org.icefaces.mobi.component.augmentedreality;

import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class AugmentedRealityLocationRenderer extends BaseLayoutRenderer{
    private static Logger logger = Logger.getLogger(AugmentedRealityLocationRenderer.class.getName());

     public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
              throws IOException {
         ResponseWriter writer = facesContext.getResponseWriter();
         String clientId = uiComponent.getClientId(facesContext);
         AugmentedRealityLocation arl = (AugmentedRealityLocation) uiComponent;

         writer.startElement(HTML.OPTION_ELEM, uiComponent);
         //in order to implement an icon in an Option, have to style or class with background image
         StringBuilder value = new StringBuilder(String.valueOf(arl.getLocationLat())).append(",");
         value.append(String.valueOf(arl.getLocationLon())).append(",").append(String.valueOf(arl.getLocationAlt()));
         value.append(",").append(arl.getLocationIcon());
         writer.writeAttribute(HTML.VALUE_ATTR, value.toString(), HTML.VALUE_ATTR);
         writer.write( arl.getLocationLabel());
         writer.startElement(HTML.OPTION_ELEM, uiComponent);
    }

}
