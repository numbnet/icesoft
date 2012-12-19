package org.icefaces.mobi.component.layoutcontainer;

import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.Utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.logging.Logger;

public class LayoutContainerRenderer extends Renderer {

    private static Logger logger = Logger.getLogger(LayoutContainerRenderer.class.getName());

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        LayoutContainer pane = (LayoutContainer) uiComponent;
        //write out root tag
        boolean single = pane.isSingle();
        boolean smallView = false;
        boolean largeView = false;

        if ( !single && checkLargeDisplay(facesContext)){
            logger.info("LARGE LAYOUT");
            largeView = true;
            pane.setActive(true);  //all the children of this component will be used.
        }
        if (single && !checkLargeDisplay(facesContext)){
            logger.info("SMALL LAYOUT");
            smallView = true;
            pane.setActive(true);  //all the children of this component will be used.
        }
        //determine device and then only render if device matches
        if ((pane.isSingle() && largeView)){
            logger.info(" this is a single and we have large view for id="+clientId+" so RETURN");
            return;
        }
        if ((!pane.isSingle() && smallView)){
            //device doesn't match the panel to be rendered so do nothing.
           logger.info(" this is a large and we have small view for id="+clientId+" so RETURN");
           return;
        }

        //write out root tag  only if double column
        if (largeView){
            writer.startElement(HTML.DIV_ELEM, uiComponent);
            writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
            // apply default style class
            StringBuilder styleClass = new StringBuilder(pane.LAYOUT_CONTAINER_CLASS);
            // user specified style class
            String userDefinedClass = pane.getStyleClass();
            if (userDefinedClass != null && userDefinedClass.length() > 0){
                styleClass.append(" ").append(userDefinedClass);
            }
            writer.writeAttribute("class", styleClass.toString(), "styleClass");

            // write out any users specified style attributes.
            writer.writeAttribute(HTML.STYLE_ATTR, pane.getStyle(), "style");
        }
    }

    private boolean checkLargeDisplay(FacesContext facesContext){
         Utils.DeviceType deviceType  = Utils.getDeviceType(facesContext);
         return (deviceType.equals(Utils.DeviceType.ipad) || deviceType.equals(Utils.DeviceType.honeycomb));
    }

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException{
        String clientId = uiComponent.getClientId(facesContext);
        LayoutContainer pane = (LayoutContainer) uiComponent;
        if (!pane.isActive()){
            return;
        }
        //otherwise, let the children be rendered
        super.encodeChildren(facesContext, uiComponent);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        LayoutContainer pane = (LayoutContainer) uiComponent;
        if (pane.isActive() &&!pane.isSingle()){
            writer.endElement(HTML.DIV_ELEM);
        }
     }

}
