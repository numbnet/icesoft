package org.icefaces.mobi.component.contentstack;

import org.icefaces.mobi.component.contentpane.ContentPane;
import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContentStackRenderer extends BaseLayoutRenderer {

   private static Logger logger = Logger.getLogger(ContentStackRenderer.class.getName());

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)throws IOException {
         ResponseWriter writer = facesContext.getResponseWriter();
         String clientId = uiComponent.getClientId(facesContext);
         ContentStack container = (ContentStack) uiComponent;
            /* write out root tag.  For current incarnation html5 semantic markup is ignored */
         writer.startElement(HTML.DIV_ELEM, uiComponent);
         writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
               // apply default style class for content-stack
         StringBuilder styleClass = new StringBuilder(ContentStack.CONTENT_WRAPPER_CLASS);
               // user specified style class
     /*    String userDefinedClass = container.getStyleClass();
         if (userDefinedClass != null && userDefinedClass.length() > 0){
              styleClass.append(" ").append(userDefinedClass);
         }
         writer.writeAttribute("class", styleClass.toString(), "styleClass");
         // write out any users specified style attributes.
         writer.writeAttribute(HTML.STYLE_ATTR, container.getStyle(), "style"); */
    }

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException{
        //all children must be of type contentPane which takes care of rendering it's children...or not
        for (UIComponent child : uiComponent.getChildren()) {
             if (!(child instanceof ContentPane) && logger.isLoggable(Level.FINER)){
                 logger.finer("all children must be of type ContentPane");
                 return;
             }
        }
        //if don't find the one asked for just show the first one. TODO
        super.renderChildren(facesContext, uiComponent);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
         ResponseWriter writer = facesContext.getResponseWriter();
         writer.endElement(HTML.DIV_ELEM);
    }
}
