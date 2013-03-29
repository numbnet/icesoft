package org.icefaces.mobi.component.scrollpane;

import org.icefaces.mobi.component.layoutcontainer.LayoutContainer;
import org.icefaces.mobi.utils.HTML;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.logging.Logger;


public class ScrollPaneRenderer extends Renderer {
      private static Logger logger = Logger.getLogger(ScrollPaneRenderer.class.getName());

      public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        ScrollPane pane = (ScrollPane) uiComponent;
        if (!panelIsActive(uiComponent))  {
            return;
        }
        //write out root tag
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        logger.info("TYPE of  PANE="+pane.getType()+ " for id="+clientId);
        // apply default style class
        StringBuilder styleClass = new StringBuilder(pane.SCROLLPANECONTENT_CLASS);
        if (pane.getType().toLowerCase().trim().equals("menu")){
            styleClass = new StringBuilder(pane.SCROLLPANEMENU_CLASS);
        }
        boolean isSingle = pane.getType().toLowerCase().trim().equals("single");
        if (isSingle){
            styleClass = new StringBuilder(pane.SCROLLPANEWRAPPER_SINGLE_CLASS);
        }
        // user specified style class
        String userDefinedClass = pane.getStyleClass();
        if (userDefinedClass != null && userDefinedClass.length() > 0){
            styleClass.append(" ").append(userDefinedClass);
        }
        writer.writeAttribute("class", styleClass.toString(), "styleClass");

        // write out any users specified style attributes.
        writer.writeAttribute(HTML.STYLE_ATTR, pane.getStyle(), "style");
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        if (isSingle){
            writer.writeAttribute("class", pane.SCROLLPANESINGLE_CLASS, null);
        }  else {
            writer.writeAttribute("class", pane.SCROLLPANE_INNER_CLASS, null);
        }
    }


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        if (!panelIsActive(uiComponent))  {
            return;
        }
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.DIV_ELEM);
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
