package org.icefaces.mobi.component.mobisx;

import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.Utils;
import org.icefaces.impl.application.AuxUploadSetup;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.logging.Logger;


public class IceMobileSXRenderer extends Renderer {
    private static Logger logger = Logger.getLogger(IceMobileSXRenderer.class.getName());
    private static String ITUNES_LINK = 
            "http://itunes.apple.com/us/app/icemobile-sx/id485908934?mt=8";

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        IceMobileSX sx = (IceMobileSX) uiComponent;
        if (Utils.showSX()){
            writer.startElement(HTML.SPAN_ELEM, uiComponent);
            writer.startElement(HTML.INPUT_ELEM, uiComponent);
            writer.writeAttribute(HTML.TYPE_ATTR, "button", HTML.TYPE_ATTR);
            writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
            StringBuilder baseClass = new StringBuilder(IceMobileSX.IMPORTANT_STYLE_CLASS);
            String styleClass = sx.getStyleClass();
            if (styleClass != null) {
                 baseClass.append(" ").append(styleClass);
            }
            writer.writeAttribute(HTML.CLASS_ATTR, baseClass.toString(), null);
            String style = sx.getStyle();

            if (style != null && style.trim().length() > 0) {
               writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);
            }
            String value = "Enable";
            Object oVal = sx.getValue();
            if (null != oVal) {
                value = oVal.toString();
            }
            writer.writeAttribute(HTML.VALUE_ATTR, value, HTML.VALUE_ATTR);

            String sessionIdParam = EnvUtils.getSafeSession(facesContext).getId();
            String uploadURL = AuxUploadSetup.getInstance().getUploadURL();
            StringBuilder sb = new StringBuilder("mobi.registerAuxUpload('");
            sb.append(sessionIdParam).append("','").append(uploadURL).append("');");
            writer.writeAttribute(HTML.ONCLICK_ATTR, sb.toString(), HTML.ONCLICK_ATTR);

            writer.endElement(HTML.INPUT_ELEM);

            //write default text if component has no children
            if (0 == uiComponent.getChildCount())  {
                writer.startElement(HTML.ANCHOR_ELEM, uiComponent);
                writer.writeAttribute(HTML.HREF_ATTR, ITUNES_LINK, null);
                writer.writeText("ICEmobile-SX", null);
                writer.endElement(HTML.ANCHOR_ELEM);
                writer.writeText(" Surf Expander.", null);
            }

            writer.endElement(HTML.SPAN_ELEM);
        }

    }


}
