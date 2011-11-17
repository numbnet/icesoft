package org.icefaces.component.list;

import org.icefaces.component.utils.BaseLayoutRenderer;
import org.icefaces.component.utils.HTML;

import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;


public class OutputListItemsRenderer extends BaseLayoutRenderer {
    private static Logger logger = Logger.getLogger(OutputListItemsRenderer.class.getName());


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        encodeList(facesContext, uiComponent);
    }

    public void encodeList(FacesContext facesContext, UIComponent uiComponent) throws IOException{
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        OutputListItems list = (OutputListItems) uiComponent;

        if (list.getVar() != null) {
            list.setRowIndex(-1);
            for (int i = 0; i < list.getRowCount(); i++) {
                //assume that if it's a list of items then it's grouped
                list.setRowIndex(i);
                writer.startElement(HTML.LI_ELEM, null);
                writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
                String userDefinedClass = list.getStyleClass();
                String styleClass = OutputListItem.OUTPUTLISTITEM_CLASS;
                if (userDefinedClass != null) {
                    styleClass += " " + userDefinedClass;
                }
                writer.writeAttribute("class", styleClass, "styleClass");
                writer.startElement(HTML.DIV_ELEM, uiComponent);
                if (list.getType().equals("thumb")) {
                    writer.writeAttribute("class", OutputListItem.OUTPUTLISTITEMTHUMB_CLASS, null);
                } else {
                    writer.writeAttribute("class", OutputListItem.OUTPUTLISTITEMDEFAULT_CLASS, null);
                }
                renderChildren(facesContext, list);
                writer.endElement(HTML.DIV_ELEM);
                writer.endElement(HTML.LI_ELEM);
            }
            list.setRowIndex(-1);
        }
        else if (facesContext.isProjectStage(ProjectStage.Development) ||
            logger.isLoggable(Level.FINER)) {
              logger.finer("OUtputListItems must define the var and value attributes");
        }
    }

   public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
       // encodeList(facesContext, uiComponent);
    }

    public boolean getRendersChildren() {
        return true;
    }
   public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
        //Rendering happens on encodeEnd
    }

}
