package org.icefaces.mobi.component.contentpane;



import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.Utils;

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

       @Override
    public void decode(FacesContext context, UIComponent component) {
        ContentPane pane = (ContentPane) component;
        String clientId = pane.getClientId(context);
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
      // no ajax behavior defined yet
  /*      String selectedPane = params.get(clientId + "_selected");
        if(selectedPane!= null) {
            pane.setSelectedId(selectedPane);
        }   */
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)throws IOException {
           ResponseWriter writer = facesContext.getResponseWriter();
           String clientId = uiComponent.getClientId(facesContext);
           ContentPane pane = (ContentPane) uiComponent;
           /* write out root tag.  For current incarnation html5 semantic markup is ignored */
           writer.startElement(HTML.DIV_ELEM, uiComponent);
           writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
           // apply default style class
           StringBuilder styleClass = new StringBuilder(pane.CONTENT_WRAPPER_CLASS);
           // user specified style class
           String userDefinedClass = pane.getStyleClass();
           if (userDefinedClass != null && userDefinedClass.length() > 0){
               styleClass.append(" ").append(userDefinedClass);
           }
           writer.writeAttribute("class", styleClass.toString(), "styleClass");

               // write out any users specified style attributes.
           writer.writeAttribute(HTML.STYLE_ATTR, pane.getStyle(), "style");

       }

       public boolean getRendersChildren() {
           return true;
       }

       public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException{
           String clientId = uiComponent.getClientId(facesContext);
           ContentPane paneController = (ContentPane) uiComponent;
           UIComponent paneToRender = null;  //all children must be panels
           UIComponent defaultToRender = null;  //first one is default
           String selectedPane = paneController.getSelectedId();

           if (paneController.getChildCount() <= 0){
                // || logger.isLoggable(Level.FINER)) {
               logger.finer("this component must have panels defined as children. Please read DOCS.");
               return;
           }else {
               logger.info("looking for selectedPane="+selectedPane);
               defaultToRender = (UIComponent)paneController.getChildren().get(0);
           }
           if (null == selectedPane){
               paneToRender = defaultToRender;
           }
           else {
               /* render first child */
               try {
                   paneToRender = paneController.findComponent(selectedPane);
               }catch (Exception e){
                   logger.info("can't find panel = "+selectedPane);
                   UIForm aform = (UIForm)Utils.findParentForm(uiComponent);
                   paneToRender=aform.findComponent(selectedPane);
               }
               if (paneToRender ==null){
                   paneToRender = defaultToRender;
               }
           }
           if (paneToRender instanceof UIPanel) {
              Utils.renderChildren(facesContext, paneToRender);
           }

       }

       public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
       throws IOException {
           ResponseWriter writer = facesContext.getResponseWriter();
           writer.endElement(HTML.DIV_ELEM);
        }

}
