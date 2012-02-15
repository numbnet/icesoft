package org.icefaces.mobi.component.panelpopup;

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


public class PanelPopupRenderer extends BaseLayoutRenderer {
    private static Logger logger = Logger.getLogger(PanelPopupRenderer.class.getName());
    private static final String JS_NAME = "panelpopup.js";
    private static final String JS_MIN_NAME = "panelpopup-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.panelpopup";

    @Override
    public void decode(FacesContext facesContext, UIComponent component) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        String clientId = component.getClientId(facesContext);
        PanelPopup panel = (PanelPopup) component;
        if (!panel.isClientSide()) {
            return;
        }
        //update with hidden field
        String submittedString = String.valueOf(requestParameterMap.get(clientId + "_hidden"));
        System.out.println("id="+clientId+" submitted visible string="+submittedString);
        if (submittedString != null) {
            boolean submittedValue = submittedString.equals("true");
            panel.setVisible(true);
        }
    }

    @Override
    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
        PanelPopup popup = (PanelPopup) component;
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = component.getClientId(facesContext);
    //    boolean clientSide= panelPopup.isClientSide();
        Map viewContextMap = facesContext.getViewRoot().getViewMap();
        if (!viewContextMap.containsKey(JS_NAME)) {
            String jsFname = JS_NAME;
            if (facesContext.isProjectStage(ProjectStage.Production)){
                jsFname = JS_MIN_NAME;
            }
            //set jsFname to min if development stage
            Resource jsFile = facesContext.getApplication().getResourceHandler().createResource(jsFname, JS_LIBRARY);
            String src = jsFile.getRequestPath();
            writer.startElement(HTML.SPAN_ELEM, component);
            writer.writeAttribute(HTML.ID_ATTR, clientId+"_libJS", HTML.ID_ATTR);
            writer.startElement("script", component);
            writer.writeAttribute("text", "text/javascript", null);
            writer.writeAttribute("src", src, null);
            writer.endElement("script");
            viewContextMap.put(JS_NAME, "true");
            writer.endElement(HTML.SPAN_ELEM);
        }
        encodeMarkup(facesContext, component);
    }

    protected void encodeMarkup(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        PanelPopup panelPopup = (PanelPopup) uiComponent;
        boolean clientSide= panelPopup.isClientSide();
        boolean visible= panelPopup.isVisible();
        if (!clientSide && !visible){
            return;
        }
        String clientId = panelPopup.getClientId(facesContext);
        StringBuilder popupBaseClass = new StringBuilder(PanelPopup.CONTAINER_CLASS);
        if (clientSide){
            popupBaseClass = new StringBuilder(PanelPopup.CLIENT_CONTAINER_CLASS);
            if (!visible){
                writer.startElement(HTML.INPUT_ELEM, uiComponent);
                writer.writeAttribute(HTML.ID_ATTR, clientId+"_open", HTML.ID_ATTR);
                writer.writeAttribute(HTML.TYPE_ATTR, "button", HTML.TYPE_ATTR);
                writer.writeAttribute(HTML.VALUE_ATTR,panelPopup.getOpenButtonLabel(), HTML.VALUE_ATTR);
                StringBuilder openClick = new StringBuilder("mobi.panelpopup.open('"+clientId+"');");
                writer.writeAttribute(HTML.ONCLICK_ATTR, openClick, HTML.ONCLICK_ATTR);
                writer.endElement(HTML.INPUT_ELEM);
            }
        }
          // div that is use to hide/show the popup screen black out--will manipulate using js

        writer.startElement(HTML.DIV_ELEM,uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_bg", HTML.ID_ATTR);
        writer.writeAttribute(HTML.CLASS_ATTR, PanelPopup.BLACKOUT_PNL_CLASS, HTML.CLASS_ATTR);
        writer.endElement(HTML.DIV_ELEM);
         //panel

        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_popup", HTML.ID_ATTR);
        writer.writeAttribute("class", popupBaseClass.toString(), "class");
          //title  if present
        String headerText = panelPopup.getHeader();
        if (headerText != null){
            writer.startElement(HTML.DIV_ELEM, uiComponent);
            writer.writeAttribute(HTML.ID_ATTR, clientId + "_title", HTML.ID_ATTR);
            writer.writeAttribute("class", PanelPopup.TITLE_CLASS, null);
            writer.write(headerText);
            if (clientSide){
                writer.startElement(HTML.INPUT_ELEM, uiComponent);
                writer.writeAttribute(HTML.TYPE_ATTR, "button", HTML.TYPE_ATTR);
                writer.writeAttribute("value", "Close", null);
                writer.writeAttribute(HTML.ONCLICK_ATTR, "mobi.panelpopup.close('"+clientId+"');", HTML.ONCLICK_ATTR);
                writer.endElement(HTML.INPUT_ELEM);
            }
            writer.endElement(HTML.DIV_ELEM);
        }

       /**   writer.startElement(HTML.DIV_ELEM, uiComponent);
          writer.writeAttribute("class", PanelPopup.INTERIOR_CONT_CLASS, null); **/
        renderChildren(facesContext, panelPopup);
        if (clientSide){
            writer.startElement(HTML.INPUT_ELEM, uiComponent);
            writer.writeAttribute(HTML.TYPE_ATTR, "hidden", HTML.TYPE_ATTR);
            writer.writeAttribute(HTML.ID_ATTR, clientId+"_hidden", HTML.ID_ATTR);
            writer.writeAttribute(HTML.NAME_ATTR, clientId+"_hidden", HTML.NAME_ATTR);
            writer.endElement(HTML.INPUT_ELEM);
        }
        writer.endElement(HTML.DIV_ELEM);
        encodeScript(facesContext, uiComponent);
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
        PanelPopup panelPopup = (PanelPopup) component;
        String clientId = panelPopup.getClientId(facesContext);
        writer.startElement(HTML.SPAN_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_scrSpan", HTML.ID_ATTR);
        writer.startElement("script", null);
        writer.writeAttribute("id", clientId+"_script", "id");
        writer.writeAttribute("text", "text/javascript", null);
        StringBuilder builder = new StringBuilder(255);
        builder.append("mobi.panelpopup.init('").append(clientId).append("', {visible: ") ;
        builder.append(panelPopup.isVisible()).append(",autocenter: ").append(panelPopup.isAutoCenter()).append("});\n");
  /*      writer.write("ice.onUnload(function(){" +
                "mobi.panelpopup.unloaded('" + clientId + "');" +
                "});\n"); */
        writer.write(builder.toString());
        writer.endElement("script");
        writer.endElement(HTML.SPAN_ELEM);
    }

}
