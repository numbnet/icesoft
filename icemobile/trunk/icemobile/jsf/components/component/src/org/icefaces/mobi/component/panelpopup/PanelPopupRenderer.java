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
package org.icefaces.mobi.component.panelpopup;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.mobi.component.button.CommandButton;
import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.JSFUtils;
import org.icefaces.mobi.utils.MobiJSFUtils;


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
        String submittedString = String.valueOf(requestParameterMap.get(clientId));
        if (submittedString != null) {
            if (submittedString.trim().equals("true") && !panel.isDisabled()){
                panel.setVisible(true);
            }else{
                panel.setVisible(false);
            }
        }
    }

    @Override
    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
        PanelPopup popup = (PanelPopup) component;
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = component.getClientId(facesContext);
        writeJavascriptFile(facesContext, component, JS_NAME, JS_MIN_NAME, JS_LIBRARY);
        encodeMarkup(facesContext, component);

    }

    protected void encodeMarkup(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        PanelPopup panelPopup = (PanelPopup) uiComponent;
        boolean clientSide = panelPopup.isClientSide();
        boolean visible = panelPopup.isVisible();
        boolean disabled = panelPopup.isDisabled();
        String clientId = panelPopup.getClientId(facesContext);
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        StringBuilder popupBaseClass = new StringBuilder(PanelPopup.HIDDEN_CONTAINER_CLASS);
        StringBuilder popupBGClass = new StringBuilder(PanelPopup.BLACKOUT_PNL_HIDDEN_CLASS);
        StringBuilder popupButtonClass = new StringBuilder(CommandButton.BASE_STYLE_CLASS);
        StringBuilder popupDisabledClass = new StringBuilder(CommandButton.DISABLED_STYLE_CLASS);
        UIComponent labelFacet = panelPopup.getFacet("label");
        if (visible) {
            popupBaseClass = new StringBuilder(PanelPopup.CONTAINER_CLASS);
            popupBGClass = new StringBuilder(PanelPopup.BLACKOUT_PNL_CLASS);
        }
        Object userClass = panelPopup.getStyleClass();
        if (null != userClass) {
            popupBGClass.append(userClass);
            popupBaseClass.append(userClass);
            popupButtonClass.append(" ").append(userClass);
            popupDisabledClass.append(" ").append(userClass);
        }
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_wrp", HTML.ID_ATTR);
        // div that is use to hide/show the popup screen black out--will manipulate using js
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_bg", HTML.ID_ATTR);
        writer.writeAttribute(HTML.CLASS_ATTR, popupBGClass.toString(), HTML.CLASS_ATTR);
        writer.endElement(HTML.DIV_ELEM);
        //panel
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_popup", HTML.ID_ATTR);

        writer.writeAttribute("class", popupBaseClass.toString(), "class");
        StringBuilder style = new StringBuilder(256);
        if (null != panelPopup.getStyle()) {
            style.append(panelPopup.getStyle());
        }
        if (style.length() > 1)  {
            writer.writeAttribute("style", style.toString(), "style");
        }
        //title  if present
        String headerText = panelPopup.getHeader();
        if (labelFacet != null || headerText != null) {
            writer.startElement(HTML.DIV_ELEM, uiComponent);
            writer.writeAttribute(HTML.ID_ATTR, clientId + "_title", HTML.ID_ATTR);
            writer.writeAttribute("class", PanelPopup.TITLE_CLASS, null);
            if (labelFacet != null) {
                JSFUtils.renderChild(facesContext, labelFacet);
            } else if (headerText != null) {
                writer.write(headerText);
            }
            writer.endElement(HTML.DIV_ELEM);
        }
        renderChildren(facesContext, panelPopup);
        if (clientSide) {
            writer.startElement(HTML.INPUT_ELEM, uiComponent);
            writer.writeAttribute(HTML.TYPE_ATTR, "hidden", HTML.TYPE_ATTR);
            writer.writeAttribute(HTML.ID_ATTR, clientId+"_hidden", HTML.ID_ATTR);
         //   writer.writeAttribute(HTML.VALUE_ATTR, String.valueOf(panelPopup.isVisible()), HTML.VALUE_ATTR);
            writer.writeAttribute(HTML.NAME_ATTR, clientId+"_hidden", HTML.NAME_ATTR);
            writer.endElement(HTML.INPUT_ELEM);
        }
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.DIV_ELEM);
        encodeScript(facesContext, panelPopup);
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

    public void encodeScript(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        PanelPopup panelPopup = (PanelPopup) component;
        String clientId = panelPopup.getClientId(facesContext);
        writer.startElement(HTML.DIV_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_scrDiv", HTML.ID_ATTR);
        writer.startElement(HTML.SPAN_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_scrSpan", HTML.ID_ATTR);
        writer.startElement("script", null);
        writer.writeAttribute("text", "text/javascript", null);
        StringBuilder builder = new StringBuilder(255);
        String hashString = String.valueOf(panelPopup.isClientSide()) + String.valueOf(panelPopup.isVisible())+
                String.valueOf(panelPopup.isAutoCenter());
        int hashcode = MobiJSFUtils.generateHashCode(hashString);
        boolean disabled = panelPopup.isDisabled();
      /*  if (disabled){
            panelPopup.setVisible(false);
        } */
        builder.append("ice.mobi.panelPopup.init('").append(clientId)
                .append("', {visible: ").append(panelPopup.isVisible())
                .append(", hash: ").append(hashcode)
                .append(",autocenter: ").append(panelPopup.isAutoCenter())
                .append(", client: ").append(panelPopup.isClientSide())
                .append(", id: '").append(panelPopup.getId()).append("'") ;
        if (panelPopup.getWidth() != Integer.MIN_VALUE){
            builder.append(", width: ").append(panelPopup.getWidth());
        }if (panelPopup.getHeight() != Integer.MIN_VALUE){
            builder.append(", height: ").append(panelPopup.getHeight());
        }
  /*      if (panelPopup.isCenterOnForm()){
            builder.append(", useForm: ").append(panelPopup.isCenterOnForm());
        }       */
        if (disabled){
            builder.append(", disabled: ").append(panelPopup.isDisabled());
        }
        builder.append("});");
        writer.write(builder.toString());
     //   logger.info(" script is="+builder.toString());
        writer.endElement("script");
        writer.endElement(HTML.SPAN_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }

}
