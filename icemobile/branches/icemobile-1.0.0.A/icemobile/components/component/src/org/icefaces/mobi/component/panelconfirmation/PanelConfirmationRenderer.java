/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icefaces.mobi.component.panelconfirmation;

import org.icefaces.mobi.utils.HTML;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * for now the css for this class is just reused from the dateSpinner popup container classes
 */
public class PanelConfirmationRenderer extends Renderer {
    private static Logger logger = Logger.getLogger(PanelConfirmationRenderer.class.getName());
    private static final String JS_NAME = "panelconfirmation.js";
    private static final String JS_MIN_NAME = "panelconfirmation-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.panelconfirmation";

    public void decode(FacesContext facesContext, UIComponent uiComponent) {

    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        PanelConfirmation panel = (PanelConfirmation) uiComponent;
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = panel.getClientId(facesContext);
        Map viewContextMap = facesContext.getViewRoot().getViewMap();
        if (!viewContextMap.containsKey(JS_NAME)) {
            String jsFname = JS_NAME;
            if (facesContext.isProjectStage(ProjectStage.Production)){
                jsFname = JS_MIN_NAME;
            }
            //set jsFname to min if development stage
            Resource jsFile = facesContext.getApplication().getResourceHandler().createResource(jsFname, JS_LIBRARY);
            String src = jsFile.getRequestPath();
            writer.startElement("script", uiComponent);
            writer.writeAttribute("text", "text/javascript", null);
            writer.writeAttribute("src", src, null);
            writer.endElement("script");
            viewContextMap.put(JS_NAME, "true");
        }
        encodePanel(writer, clientId, uiComponent);

    }

    private void encodePanel(ResponseWriter writer, String clientId, UIComponent uiComponent) throws IOException{
        PanelConfirmation panel = (PanelConfirmation) uiComponent;
        StringBuilder popupBaseClass = new StringBuilder(PanelConfirmation.CONTAINER_HIDE_CLASS);
        // div that is use to hide/show the popup screen black out--will manipulate using js
        writer.startElement(HTML.DIV_ELEM,uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_bg", HTML.ID_ATTR);
        writer.writeAttribute(HTML.CLASS_ATTR, PanelConfirmation.BLACKOUT_PNL_HIDE_CLASS, HTML.CLASS_ATTR);
        writer.endElement(HTML.DIV_ELEM);
       //panel
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_popup", HTML.ID_ATTR);
        writer.writeAttribute("class", popupBaseClass.toString(), "class");
        //title
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_title", HTML.ID_ATTR);
        writer.writeAttribute("class", PanelConfirmation.TITLE_CLASS, null);
        writer.write(panel.getTitle());
        writer.endElement(HTML.DIV_ELEM);
        //message
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute("class", PanelConfirmation.SELECT_CONT_CLASS, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_msg", HTML.ID_ATTR);
        writer.write(panel.getMessage());
        writer.endElement(HTML.DIV_ELEM);
        //button container
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute("class", PanelConfirmation.BUTTON_CONT_CLASS, null);
        String type = panel.getType();
        if (type != null) {
            if (type.equalsIgnoreCase("acceptOnly")) {
                renderAcceptButton(writer, uiComponent, panel.getAcceptLabel(), clientId);
            } else if (type.equalsIgnoreCase("cancelOnly")) {
                renderCancelButton(writer, uiComponent, panel.getCancelLabel(), clientId);
            } else {
                renderAcceptButton(writer, uiComponent, panel.getAcceptLabel(), clientId);
                renderCancelButton(writer, uiComponent, panel.getCancelLabel(), clientId);
            }
        } else {
            renderAcceptButton(writer, uiComponent, panel.getAcceptLabel(), clientId);
            renderCancelButton(writer, uiComponent, panel.getCancelLabel(), clientId);
        }
        writer.endElement(HTML.DIV_ELEM);
        writer.startElement(HTML.SCRIPT_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_script", HTML.ID_ATTR);

        writer.endElement(HTML.SCRIPT_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }

    private void renderAcceptButton(ResponseWriter writer, UIComponent uiComponent, String value, String id) throws IOException{
        writer.startElement("input", uiComponent);
        writer.writeAttribute("class", PanelConfirmation.BUTTON_ACCEPT_CLASS, null);
        writer.writeAttribute(HTML.ID_ATTR,id+"_accept", HTML.ID_ATTR);
        writer.writeAttribute ("type", "button", "type");
        writer.writeAttribute("value", value, null);
        writer.writeAttribute(HTML.ONCLICK_ATTR, "mobi.panelConf.confirm('"+id+"');", null);
        writer.endElement("input");
    }

    private void renderCancelButton(ResponseWriter writer, UIComponent uiComponent, String value, String id) throws IOException{
        writer.startElement("input", uiComponent);
        writer.writeAttribute("class", PanelConfirmation.BUTTON_CANCEL_CLASS, null);
        writer.writeAttribute(HTML.ID_ATTR, id+"_cancel", HTML.ID_ATTR);
        writer.writeAttribute ("type", "button", "type");
        writer.writeAttribute("value", value, null);
        writer.writeAttribute(HTML.ONCLICK_ATTR, "mobi.panelConf.close('"+id+"');", null);
        writer.endElement("input");
    }

    public static StringBuilder renderOnClickString(UIComponent uiComponent, StringBuilder origOnClickCall){
        String panelConfirmationId = String.valueOf(uiComponent.getAttributes().get("panelConfirmation"));
        String callCompId = uiComponent.getClientId();
        PanelConfirmation panelConfirmation = (PanelConfirmation) uiComponent.findComponent(panelConfirmationId);
        if (panelConfirmation != null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            panelConfirmationId = panelConfirmation.getClientId(facesContext);
            String autoCenter = panelConfirmation.isAutoCenter() ? "true" : "false";
            StringBuilder sb = new StringBuilder("mobi.panelConf.init('").append(panelConfirmationId).append("','");
            sb.append(callCompId).append("',").append(autoCenter).append(",").append(origOnClickCall);
            sb.append("});");
            return sb;
        } else {
            return null;
        }
    }

}
