/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

package org.icemobile.renderkit;

import org.icemobile.component.IPanelPopup;


import java.io.IOException;
import java.util.logging.Logger;
import org.icemobile.util.Utils;

import static org.icemobile.util.HTML.*;

public class PanelPopupCoreRenderer extends BaseCoreRenderer {
    //private static final Logger logger = Logger.getLogger(PanelPopupCoreRenderer.class.toString());

    public void encodeBegin(IPanelPopup component, IResponseWriter writer)
            throws IOException {
        IPanelPopup panelPopup = (IPanelPopup) component;
        boolean visible = panelPopup.isVisible();
        String clientId = panelPopup.getClientId();
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(ID_ATTR, clientId);
        StringBuilder popupBaseClass = new StringBuilder(IPanelPopup.HIDDEN_CONTAINER_CLASS);
        StringBuilder popupBGClass = new StringBuilder(IPanelPopup.BLACKOUT_PNL_HIDDEN_CLASS);
        //leave user class appending here, but it's actually done in js
        if (visible) {
            popupBaseClass = new StringBuilder(IPanelPopup.CONTAINER_CLASS);
            popupBGClass = new StringBuilder(IPanelPopup.BLACKOUT_PNL_CLASS);
        }
        String userClass = panelPopup.getStyleClass();
        if (null != userClass) {
          //  popupBGClass.append(String.valueOf(userClass));
            popupBaseClass.append(userClass);
        }
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(ID_ATTR, clientId+"_wrp");
        // div that is use to hide/show the popup screen black out--will manipulate using js
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(ID_ATTR, clientId + "_bg");
        writer.writeAttribute(CLASS_ATTR, popupBGClass.toString());
        writer.endElement(DIV_ELEM);
        //panel
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(ID_ATTR, clientId + "_popup");
        writer.writeAttribute("class", popupBaseClass.toString());
        if (null != panelPopup.getStyle()) {
             writer.writeAttribute(STYLE_ATTR, panelPopup.getStyle());
        }

    }



    public void encodeEnd(IPanelPopup  panelPopup, IResponseWriter writer)
            throws IOException{
        boolean clientSide = panelPopup.isClientSide();
        String clientId= panelPopup.getClientId();
        if (clientSide) {
            writer.startElement(INPUT_ELEM, panelPopup);
            writer.writeAttribute(TYPE_ATTR, "hidden");
            writer.writeAttribute(ID_ATTR, clientId+"_hidden");
            writer.writeAttribute(VALUE_ATTR, panelPopup.isClientSide() ? "false" : String.valueOf(panelPopup.isVisible()));
            writer.writeAttribute(NAME_ATTR, clientId+"_hidden");
            writer.endElement(INPUT_ELEM);
        }
        writer.endElement(DIV_ELEM);
        writer.endElement(DIV_ELEM);
        encodeScript(panelPopup, writer);
        writer.endElement(DIV_ELEM);
    }

    public void encodeScript(IPanelPopup panelPopup, IResponseWriter writer)
          throws IOException{
        String clientId = panelPopup.getClientId();
        writer.startElement(DIV_ELEM, panelPopup);
        writer.writeAttribute(ID_ATTR, clientId+"_scrDiv");
        writer.writeAttribute(CLASS_ATTR, "mobi-hidden");
        writer.startElement(SPAN_ELEM, panelPopup);
        writer.writeAttribute(ID_ATTR, clientId + "_scrSpan");
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript");
        StringBuilder builder = new StringBuilder(255);
        boolean disabled = panelPopup.isDisabled();
        if (disabled && !panelPopup.isClientSide()){
            panelPopup.setVisible(false);
        }
        builder.append("ice.mobi.panelPopup.init('").append(clientId)
                .append("', {visible: ").append(panelPopup.isVisible())
                .append(", autocenter: ").append(panelPopup.isAutoCenter())
                .append(", client: ").append(panelPopup.isClientSide())
                .append(", id: '").append(panelPopup.getId()).append("'") ;
        if (panelPopup.getWidth() != Integer.MIN_VALUE){
            builder.append(", width: ").append(panelPopup.getWidth());
        }if (panelPopup.getHeight() != Integer.MIN_VALUE){
            builder.append(", height: ").append(panelPopup.getHeight());
        }
        if (null !=panelPopup.getStyleClass()){
            builder.append(", sclass: '").append(panelPopup.getStyleClass()).append("'");
        }
        if (panelPopup.getStyle()!=null){
            builder.append(", style: '").append(panelPopup.getStyle()).append("'");
        }
        if (disabled){
            builder.append(", disabled: ").append(panelPopup.isDisabled());
        }
        builder.append("});");
        writer.write(builder.toString());
        writer.endElement("script");
        writer.endElement(SPAN_ELEM);
        writer.endElement(DIV_ELEM);
    }

}
