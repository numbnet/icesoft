package org.icefaces.mobi.component.accordion;

import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.Utils;

import javax.faces.component.UIComponent;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;


public class AccordionRenderer extends BaseLayoutRenderer {
    private static Logger logger = Logger.getLogger(AccordionRenderer.class.getName());
    private static final String JS_NAME = "accordion.js";
    private static final String JS_MIN_NAME = "accordion-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.accordion";

    @Override
    public void decode(FacesContext context, UIComponent component) {
         Accordion accordion = (Accordion) component;
         String clientId = accordion.getClientId(context);
         Map<String, String> params = context.getExternalContext().getRequestParameterMap();
       // no ajax behavior defined yet
         String indexStr = params.get(clientId + "_hidden");
         int oldIndex = accordion.getActiveIndex();
         if( null != indexStr) {
             //find the activeIndex and set it
             int index = findIndex(context, accordion, oldIndex, indexStr);
             if (oldIndex!=index){
                 accordion.setActiveIndex(index);
                 ///will eventually queue the PanechangeEvent here
             }
         }
     }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Accordion accordion = (Accordion) uiComponent;
        writeJavascriptFile(facesContext, uiComponent, JS_NAME, JS_MIN_NAME, JS_LIBRARY);
        /* write out root tag.  For current incarnation html5 semantic markup is ignored */
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        // apply default style class
        StringBuilder styleClass = new StringBuilder(accordion.ACCORDION_CLASS);
        // user specified style class
        String userDefinedClass = accordion.getStyleClass();
        if (userDefinedClass != null && userDefinedClass.length() > 0){
                styleClass.append(" ").append(userDefinedClass);
        }
        writer.writeAttribute("class", styleClass.toString(), "styleClass");
        // write out any users specified style attributes.
        writer.writeAttribute(HTML.STYLE_ATTR, accordion.getStyle(), "style");
        // need to set opened panel
        encodeDataOpenedAttribute(facesContext, uiComponent);
    }

    public boolean getRendersChildren() {
        return true;
    }
    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException{
         Utils.renderChildren(facesContext, uiComponent);
    }
    public void encodeDataOpenedAttribute(FacesContext facesContext, UIComponent uiComponent) throws IOException{
        Accordion paneController = (Accordion) uiComponent;
        ResponseWriter writer = facesContext.getResponseWriter();
        UIComponent openPane = null;  //all children must be panels
        int activeIndex = paneController.getActiveIndex();

        if (paneController.getChildCount() <= 0){
                 // || logger.isLoggable(Level.FINER)) {
            logger.finer("this component must have panels defined as children. Please read DOCS.");
                return;
        } //check whether we have exceeded maximum number of children for accordion???
        openPane = (UIComponent)paneController.getChildren().get(activeIndex);
 //       logger.info("looking for index="+activeIndex+" selectedPane to open ="+openPane.getId());
        //selectedPanel is now set
        writer.writeAttribute("data-opened", openPane.getClientId(facesContext), null);
    }

     public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException {
         ResponseWriter writer = facesContext.getResponseWriter();
         encodeHidden(facesContext, uiComponent);
         writer.endElement(HTML.DIV_ELEM);
         encodeScript(facesContext, uiComponent);
     }

     public void encodeScript(FacesContext context, UIComponent uiComponent) throws IOException {
        //need to initialize the component on the page and can also
        ResponseWriter writer = context.getResponseWriter();
        Accordion pane = (Accordion) uiComponent;
        String clientId = pane.getClientId(context);
        writer.startElement("span", uiComponent);
        writer.writeAttribute("id", clientId + "_script", "id");
        writer.startElement("script", null);
        writer.writeAttribute("text", "text/javascript", null);
        StringBuilder cfg = new StringBuilder("{singleSubmit: false");
        boolean autoheight = pane.isAutoHeight();
        cfg.append(", autoheight: ").append(autoheight);
        cfg.append(", maxheight: '").append(pane.getFixedHeight()).append("'");
        cfg.append("}");
         //just have to add behaviors if we are going to use them.
        writer.write("mobi.accordionController.initClient('" + clientId + "'," +cfg.toString()+");");
        writer.endElement("script");
        writer.endElement("span");
    }

    /**
     * move this into one of the base renderer classes for reuse
     * @param context
     * @param uiComponent
     * @throws IOException
     */
    public void encodeHidden(FacesContext context, UIComponent uiComponent) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Accordion accordion = (Accordion) uiComponent;
        String clientId = accordion.getClientId(context);
        writer.startElement("span", uiComponent);
        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "hidden", "type");
        writer.writeAttribute("id", clientId+"_hidden", "id");
        writer.writeAttribute("name", clientId+"_hidden", "name");
        writer.endElement("input");
        writer.endElement("span");
    }
    private int findIndex(FacesContext context, Accordion accordion, int oldIndex, String id){
        for (int i=0; i < accordion.getChildCount();i++){
            if (accordion.getChildren().get(i).getClientId(context).equals(id)){
                  return i;
            }
        }
        return oldIndex;
    }
}
