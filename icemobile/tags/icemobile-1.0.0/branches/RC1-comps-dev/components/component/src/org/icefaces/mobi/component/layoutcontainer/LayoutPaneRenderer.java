package org.icefaces.mobi.component.layoutcontainer;


import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.component.layoutcontainer.LayoutContainer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.logging.Logger;

public class LayoutPaneRenderer extends Renderer {

    private static Logger logger = Logger.getLogger(LayoutPaneRenderer.class.getName());

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        LayoutPane pane = (LayoutPane) uiComponent;

        if (!panelIsActive(uiComponent))  {
            return;
        }
        String layoutType = pane.getType();
        //write out root tag
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        // apply default style class
        LayoutPane.PanelType paneType = checkPanelType(pane.getType().toLowerCase().trim());
        StringBuilder styleClass = new StringBuilder(pane.LAYOUT_CONTAINER_HEADER_CLASS);
        // user specified style class
        if (pane.getType().toLowerCase().trim().equals("footer")){
            styleClass = new StringBuilder(pane.LAYOUT_CONTAINER_FOOTER_CLASS);
        }
        String userDefinedClass = pane.getStyleClass();
        if (userDefinedClass != null && userDefinedClass.length() > 0){
            styleClass.append(" ").append(userDefinedClass);
        }
        writer.writeAttribute("class", styleClass.toString(), "styleClass");

        // write out any users specified style attributes.
        writer.writeAttribute(HTML.STYLE_ATTR, pane.getStyle(), "style");
    }



    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        if (!panelIsActive(uiComponent))  {
            return;
        }
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HTML.DIV_ELEM);
     }

     private LayoutPane.PanelType checkPanelType(String pane){
        if (pane.equals(LayoutPane.PanelType.header)) return LayoutPane.PanelType.header;
        if (pane.equals(LayoutPane.PanelType.footer)) return LayoutPane.PanelType.footer;
        return LayoutPane.PanelType.DEFAULT;
     }

     private boolean panelIsActive(UIComponent uiComponent){
        UIComponent parent = uiComponent.getParent();
        //only render this if the layoutcontainer has this part of the markup for the tree
        if (!(parent instanceof LayoutContainer)) {
            logger.info("This component must have parent of LayoutContainer");
            return false;
        }
        LayoutContainer lc = (LayoutContainer)parent;
        if (lc.isActive()){
            return true;
        }
        else {
            return false;
        }

     }

}
