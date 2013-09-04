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

import org.icemobile.component.IPanelConfirmation;
import org.icemobile.component.IPanelPopup;
import org.icemobile.util.CSSUtils;
import org.icemobile.util.Utils;

import java.io.IOException;
import java.util.logging.Logger;

import static org.icemobile.util.HTML.*;

public class PanelConfirmationCoreRenderer extends BaseCoreRenderer {
    private static final Logger logger =
            Logger.getLogger(PanelConfirmationCoreRenderer.class.toString());

    public void encodeEnd(IPanelConfirmation panel, IResponseWriter writer)
            throws IOException{
      //  String clientId = panel.getClientId();
        encodePanel(panel, writer);

    }

    private void encodePanel(IPanelConfirmation panel, IResponseWriter writer) throws IOException {
        // div that is use to hide/show the popup screen black out--will manipulate using js
        String clientId = panel.getClientId();
        writer.startElement(DIV_ELEM, panel);
        writer.writeAttribute(ID_ATTR, clientId + "_bg");
        writer.writeAttribute(CLASS_ATTR, IPanelConfirmation.BLACKOUT_PNL_HIDE_CLASS);
        writer.endElement(DIV_ELEM);
        //panel
        writer.startElement(DIV_ELEM, panel);
        writer.writeAttribute(ID_ATTR, clientId + "_popup");
        StringBuilder containerClass = new StringBuilder(IPanelConfirmation.CONTAINER_HIDE_CLASS);
        if (null != panel.getStyleClass()){
            containerClass.append(" ").append(panel.getStyleClass());
        }
        writer.writeAttribute(CLASS_ATTR, containerClass.toString());
        writer.writeAttribute(STYLE_ATTR, panel.getStyle());
        //title
        writer.startElement(DIV_ELEM, panel);
        writer.writeAttribute(ID_ATTR, clientId + "_title");
        writer.writeAttribute(CLASS_ATTR, IPanelConfirmation.TITLE_CLASS + " " + CSSUtils.STYLECLASS_BAR_B);
        writer.write(panel.getTitle());
        writer.endElement(DIV_ELEM);
        //message
        writer.startElement(DIV_ELEM, panel);
        writer.writeAttribute(CLASS_ATTR, IPanelConfirmation.SELECT_CONT_CLASS);
        writer.writeAttribute(ID_ATTR, clientId + "_msg");
        writer.write(panel.getMessage());
        writer.endElement(DIV_ELEM);
        //button container
        writer.startElement(DIV_ELEM, panel);
        writer.writeAttribute(CLASS_ATTR, IPanelConfirmation.BUTTON_CONT_CLASS);
        String type = panel.getType();
        if (type != null) {
            if (type.equalsIgnoreCase("acceptOnly")) {
                renderAcceptButton(writer, panel, panel.getAcceptLabel(), clientId);
            } else if (type.equalsIgnoreCase("cancelOnly")) {
                renderCancelButton(writer, panel, panel.getCancelLabel(), clientId);
            } else {
                renderAcceptButton(writer, panel, panel.getAcceptLabel(), clientId);
                renderCancelButton(writer, panel, panel.getCancelLabel(), clientId);
            }
        } else {
            renderAcceptButton(writer, panel, panel.getAcceptLabel(), clientId);
            renderCancelButton(writer, panel, panel.getCancelLabel(), clientId);
        }
        writer.endElement(DIV_ELEM);
        writer.startElement(SCRIPT_ELEM, panel);
        writer.writeAttribute(ID_ATTR, clientId + "_script");

        writer.endElement(SCRIPT_ELEM);
        writer.endElement(DIV_ELEM);
    }

    private void renderAcceptButton(IResponseWriter writer, IPanelConfirmation panel, String value, String id) throws IOException {
        writer.startElement(INPUT_ELEM, panel);
        writer.writeAttribute("class", IPanelConfirmation.BUTTON_ACCEPT_CLASS);
        writer.writeAttribute(ID_ATTR, id + "_accept");
        writer.writeAttribute(TYPE_ATTR, "button");
        writer.writeAttribute(VALUE_ATTR, value);
        StringBuilder sb = new StringBuilder("ice.mobi.panelConf.confirm('");
        sb.append(id).append("');") ;
        writer.writeAttribute(ONCLICK_ATTR, sb.toString());
        writer.endElement(INPUT_ELEM);
    }

    private void renderCancelButton(IResponseWriter writer, IPanelConfirmation panel, String value, String id) throws IOException {
        writer.startElement(INPUT_ELEM, panel);
        writer.writeAttribute(CLASS_ATTR, IPanelConfirmation.BUTTON_CANCEL_CLASS);
        writer.writeAttribute(ID_ATTR, id + "_cancel");
        writer.writeAttribute(TYPE_ATTR, "button");
        writer.writeAttribute(VALUE_ATTR, value);
        writer.writeAttribute(ONCLICK_ATTR, "ice.mobi.panelConf.close('" + id + "');");
        writer.endElement(INPUT_ELEM);
    }

}
