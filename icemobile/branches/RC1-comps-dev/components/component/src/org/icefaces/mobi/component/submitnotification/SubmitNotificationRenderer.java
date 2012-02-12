package org.icefaces.mobi.component.submitnotification;

import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;


public class SubmitNotificationRenderer extends BaseLayoutRenderer {
    private static final Logger logger =
             Logger.getLogger(SubmitNotificationRenderer.class.toString());
    private static final String JS_NAME = "submitnotification.js";
    private static final String JS_MIN_NAME = "submitnotification-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.submitnotification";

    @Override
      public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
          SubmitNotification popup = (SubmitNotification) component;
          ResponseWriter writer = facesContext.getResponseWriter();
          String clientId = component.getClientId(facesContext);
      //    boolean clientSide= panelNotify.isClientSide();
          writeJavascriptFile(facesContext, component, JS_NAME, JS_MIN_NAME, JS_LIBRARY);
          encodeMarkup(facesContext, component);
      }

    protected void encodeMarkup(FacesContext facesContext, UIComponent uiComponent) throws IOException {
          ResponseWriter writer = facesContext.getResponseWriter();
          SubmitNotification panelNotify = (SubmitNotification) uiComponent;
          String clientId = panelNotify.getClientId(facesContext);
          StringBuilder popupBaseClass = new StringBuilder(SubmitNotification.CONTAINER_HIDE_CLASS);
          // div that is use to hide/show the popup screen black out--will manipulate using js
          writer.startElement(HTML.DIV_ELEM,uiComponent);
          writer.writeAttribute(HTML.ID_ATTR, clientId + "_bg", HTML.ID_ATTR);
          writer.writeAttribute(HTML.CLASS_ATTR, SubmitNotification.BLACKOUT_PNL_HIDE_CLASS, HTML.CLASS_ATTR);
          writer.endElement(HTML.DIV_ELEM);
           //panel
          writer.startElement(HTML.DIV_ELEM, uiComponent);
          writer.writeAttribute(HTML.ID_ATTR, clientId+"_popup", HTML.ID_ATTR);
          writer.writeAttribute("class", popupBaseClass.toString(), "class");
          renderChildren(facesContext, panelNotify);
          writer.endElement(HTML.DIV_ELEM);
      }


      @Override
      public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
          //Rendering happens on encodeEnd
      }

      @Override
      public boolean getRendersChildren() {
          return true;
      }

      public void encodeScript(FacesContext facesContext, UIComponent component) throws IOException{
          ResponseWriter writer = facesContext.getResponseWriter();
          SubmitNotification panelNotify = (SubmitNotification) component;
          String clientId = panelNotify.getClientId(facesContext);
          writer.startElement(HTML.SPAN_ELEM, component);
          writer.writeAttribute(HTML.ID_ATTR, clientId+"_scrSpan", HTML.ID_ATTR);
          writer.startElement("script", null);
          writer.writeAttribute("id", clientId+"_script", "id");
          writer.writeAttribute("text", "text/javascript", null);
          StringBuilder builder = new StringBuilder(255);
          builder.append("mobi.submitnotify.init('").append(clientId).append("');") ;
          writer.write(builder.toString());
          writer.endElement("script");
          writer.endElement(HTML.SPAN_ELEM);
      }
      public static String findSubmitNotificationId(UIComponent uiComponent, String subNotId){
        SubmitNotification panelNotification = (SubmitNotification) uiComponent.findComponent(subNotId);
        if (panelNotification != null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String panelNotificationId = panelNotification.getClientId(facesContext);
            String sb = new String(panelNotificationId);
            return sb;
        } else {
            return null;
        }
    }


}
