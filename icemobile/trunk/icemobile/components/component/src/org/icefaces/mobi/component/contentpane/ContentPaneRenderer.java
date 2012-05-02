package org.icefaces.mobi.component.contentpane;

import org.icefaces.mobi.component.accordion.Accordion;
import org.icefaces.mobi.component.contentstack.ContentStack;
import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.Utils;
import org.icefaces.mobi.api.ContentPaneController;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;


public class ContentPaneRenderer extends BaseLayoutRenderer {

    private static Logger logger = Logger.getLogger(ContentPaneRenderer.class.getName());

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)throws IOException {
        Object parent = uiComponent.getParent();
        ContentPane pane = (ContentPane) uiComponent;
        String cacheType = checkCacheType(pane.getCacheType().toLowerCase().trim());
        if (parent instanceof Accordion){
              Accordion accordion = (Accordion)parent;
              //eventually this will be replaced by facet to allow developer to design their own?
              encodePaneAccordionHandle(facesContext, uiComponent, accordion);
        }else {
            ResponseWriter writer = facesContext.getResponseWriter();
            String clientId = uiComponent.getClientId(facesContext);
            String contentClass = ContentPane.CONTENT_HIDDEN_CLASS.toString();
            /* write out root tag.  For current incarnation html5 semantic markup is ignored */
            writer.startElement(HTML.DIV_ELEM, uiComponent);
            writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
            // apply default style class for panel-stack  ??
            //if cacheType is client and not selected must use invisible rendering
            if (iAmSelected(facesContext, uiComponent)){
                 contentClass = ContentPane.CONTENT_BASE_CLASS.toString();
            }
            writer.writeAttribute("class", contentClass, "class");

                   // write out any users specified style attributes.
            writer.writeAttribute(HTML.STYLE_ATTR, pane.getStyle(), "style");
        }
    }

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException{
        ContentPane pane = (ContentPane)uiComponent;
        String cacheType = checkCacheType(pane.getCacheType().toLowerCase().trim());

        //if I am clientSide, I will always be present and always render
        if (cacheType.equals("client")){
            logger.info("cacheType is client for id="+uiComponent.getId());
            super.renderChildren(facesContext, uiComponent);
            return;
        }
        //am I the selected pane?  Can I count on the taghandler to already have
        //things constructed?? assume so for now.
    //    logger.info("selectedId="+selectedId+" clientId="+clientId);
        if (iAmSelected(facesContext, uiComponent)){
            super.renderChildren(facesContext, uiComponent);
        }
    }


    private boolean iAmSelected(FacesContext facesContext, UIComponent uiComponent){
        Object parent = uiComponent.getParent();
        String selectedId= null;
        //might change this to just test if it implements the interface....
        if (parent instanceof ContentPaneController){
           // logger.info(" instance of ContentPaneController");
            ContentPaneController paneController = (ContentPaneController)parent;
            selectedId = paneController.getSelectedId();
        }
        else {
            logger.info("Parent must implement ContentPaneController-> has instead="+parent.getClass().getName());
            return false;
        }
        ContentPane pane = (ContentPane)uiComponent;
        String clientId = uiComponent.getClientId(facesContext);
        return (selectedId.equals(clientId));
    }

    private String checkCacheType(String type){
        if (type.equals(ContentPane.CacheType.client.name())) return ContentPane.CacheType.client.name();
        if (type.equals(ContentPane.CacheType.constructed.name())) return ContentPane.CacheType.constructed.name();
        if (type.equals(ContentPane.CacheType.tobeconstructed.name())) return ContentPane.CacheType.tobeconstructed.name();
        return ContentPane.CacheType.DEFAULT.name();
    }


    /* if we only want to render the selected child of the accordion */
    public void encodeAccordionChildren(FacesContext facesContext, UIComponent uiComponent, Object parent)
           throws IOException {
        Accordion accordion = (Accordion)parent;
        int activeIndex = accordion.getActiveIndex();
        if (accordion.getChildCount() <=0){
                // || logger.isLoggable(Level.FINER)) {
            logger.finer("this component must have panels defined as children. Please read DOCS.");
            return;
        }
        UIComponent defaultPane = accordion.getChildren().get(0);
        if (activeIndex>0 && activeIndex <=accordion.getChildCount()) {
            defaultPane = accordion.getChildren().get(activeIndex);
        }
        if (defaultPane instanceof UIPanel) {
            Utils.renderChildren(facesContext, defaultPane);
        }


    }


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
          throws IOException {
         ResponseWriter writer = facesContext.getResponseWriter();
         if (uiComponent.getParent() instanceof Accordion){
             writer.endElement(HTML.SECTION_ELEM);
             return;
         }
         writer.endElement(HTML.DIV_ELEM);
    }

    public void encodePaneAccordionHandle(FacesContext facesContext, UIComponent uiComponent, Accordion accordion)
        throws IOException{
        // only selected pane is open/active
         int activeIndex = accordion.getActiveIndex();
         String imOpened = accordion.getChildren().get(activeIndex).getId();
         String accordionId = accordion.getClientId(facesContext);
         ResponseWriter writer = facesContext.getResponseWriter();
         String clientId = uiComponent.getClientId(facesContext);
         ContentPane pane = (ContentPane) uiComponent;
         String cacheType = checkCacheType(pane.getCacheType().toLowerCase().trim());
         boolean client = false;
         if (cacheType.equals("client")){
              client=true;
         }
         String myId = pane.getId();
         String handleClass = "handle";
         String pointerClass = "pointer";
         writer.startElement(HTML.SECTION_ELEM, uiComponent);
         writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
            // apply default style class for panel-stack
         StringBuilder styleClass = new StringBuilder("closed");
         if (imOpened.equals(myId)){
             styleClass = new StringBuilder("open");
         }
               // user specified style class
         String userDefinedClass = pane.getStyleClass();
         if (userDefinedClass != null && userDefinedClass.length() > 0){
              styleClass.append(" ").append(userDefinedClass);
              handleClass+= " " + userDefinedClass;
              pointerClass+=" " + userDefinedClass;
         }
         writer.writeAttribute("class", styleClass.toString(), "class");
         writer.writeAttribute(HTML.STYLE_ATTR, pane.getStyle(), "style");
         writer.startElement(HTML.DIV_ELEM, uiComponent);
         writer.writeAttribute("class", handleClass, "class");
         writer.writeAttribute("onclick", "mobi.accordionController.toggleClient('"+accordionId+"',this,"+client+");","onclick");
        //may want to do touch support??
         writer.startElement(HTML.DIV_ELEM, uiComponent);
         writer.writeAttribute("class", pointerClass, "class");
         writer.write(Accordion.ACCORDION_RIGHT_POINTING_POINTER);
         writer.endElement(HTML.DIV_ELEM);
         String title = pane.getAccordionTitle();
         writer.write(title);
         writer.endElement(HTML.DIV_ELEM);
    }

}
