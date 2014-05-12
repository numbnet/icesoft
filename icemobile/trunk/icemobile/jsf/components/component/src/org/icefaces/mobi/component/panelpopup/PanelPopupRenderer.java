/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.JSFUtils;
import org.icemobile.renderkit.IResponseWriter;
import org.icemobile.renderkit.PanelPopupCoreRenderer;
import org.icemobile.util.CSSUtils;


public class PanelPopupRenderer extends BaseLayoutRenderer {
    //private static final Logger logger = Logger.getLogger(PanelPopupRenderer.class.getName());

    @Override
    public void decode(FacesContext facesContext, UIComponent component) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        String clientId = component.getClientId(facesContext);
        PanelPopup panel = (PanelPopup) component;
        if (!panel.isClientSide()) {
            return;
        }
        //update with hidden field
        String submittedString = String.valueOf(requestParameterMap.get(clientId+"_hidden"));
        if (submittedString != null && !isValueBlank(submittedString)) {
            if (submittedString.trim().equals("true") && !panel.isDisabled()){
                panel.setVisible(true);
            }else{
                panel.setVisible(false);
            }
        }
    }

    @Override
    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
        PanelPopup panelPopup = (PanelPopup) component;
        IResponseWriter writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        PanelPopupCoreRenderer renderer = new PanelPopupCoreRenderer();
        renderer.encodeBegin(panelPopup, writer);
        UIComponent labelFacet = panelPopup.getFacet("label");
        String headerText = panelPopup.getHeaderText();
        if (labelFacet != null || headerText != null) {
            writer.startElement(HTML.DIV_ELEM, component);
            writer.writeAttribute(HTML.ID_ATTR, panelPopup.getClientId() + "_title");
            writer.writeAttribute("class", PanelPopup.TITLE_CLASS + " " + CSSUtils.STYLECLASS_BAR_B);
            if (labelFacet != null) {
                JSFUtils.renderChild(facesContext, labelFacet);
            } else if (headerText != null) {
                writer.write(headerText);
            }
            writer.endElement(HTML.DIV_ELEM);
        }
        boolean clientSide = panelPopup.isClientSide();
        boolean visible = panelPopup.isVisible();
        if (clientSide || visible) {
            renderChildren(facesContext, panelPopup);
        }
        renderer.encodeEnd(panelPopup, writer);
    }

    @Override
    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
        //Rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

}
