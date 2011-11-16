package org.icefaces.component.list;

import org.icefaces.component.utils.BaseLayoutRenderer;
import org.icefaces.component.utils.HTML;

import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class OutputListItemsRenderer extends BaseLayoutRenderer {
    private static Logger logger = Logger.getLogger(OutputListItemsRenderer.class.getName());

  /*  public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        OutputListItems list = (OutputListItems) uiComponent;

        // apply component style classes.
     /*     String userDefinedClass = list.getStyleClass();
      StringBuilder styleClasses = new StringBuilder(OutputList.OUTPUTLIST_CLASS);
        if (list.isInset()) {
            styleClasses.append(" ").append(OutputList.OUTPUTLISTINSET_CLASS);
        }
        if (userDefinedClass != null) {
            styleClasses.append(" ").append(userDefinedClass);
        }
        writer.writeAttribute("class", styleClasses.toString(), "styleClass"); */


        }/* else  if (facesContext.isProjectStage(ProjectStage.Development) ||
                logger.isLoggable(Level.FINER)) {  */
        //check for value of the var and if not null then iterate over list otherwise,
        //xlook for appropirate children and
        // verify the children are OutputListItem only
       /*     List<UIComponent> children = uiComponent.getChildren();
            for (UIComponent child : children) {
                if (!(child instanceof OutputListItem)) {
                    logger.finer("The OutputList component allows only children of type OutputListItem");
                }
            }
        }
    }  */


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
       // ResponseWriter writer = facesContext.getResponseWriter();
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
                //do we want to allow them to overwrite the styling with a list??
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
            }
            list.setRowIndex(-1);
            writer.endElement(HTML.DIV_ELEM);
            writer.endElement(HTML.LI_ELEM);
    }
  /*  public boolean getRendersChildren() {
        return false;
    }  */

  /*   public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
        OutputList list = (OutputList) component;
         if (list.getVar() !=null ) {
             //Rendering happens on encodeEnd
             return;
         }
        super.encodeChildren(facesContext, component);

    }*/
}
