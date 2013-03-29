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

import org.icemobile.component.ISubmitNotification;

import java.io.IOException;
import java.util.logging.Logger;

import static org.icemobile.util.HTML.*;
import org.icemobile.component.IMenuButton;

public class MenuButtonCoreRenderer extends BaseCoreRenderer {
    private static final Logger logger =
            Logger.getLogger(MenuButtonCoreRenderer.class.toString());

    public void encodeBegin(IMenuButton menu, IResponseWriter writer)
            throws IOException{
        String clientId = menu.getClientId();
        writer.startElement(DIV_ELEM, menu);
        writer.writeAttribute(ID_ATTR, clientId);
        // apply button type style classes
        StringBuilder baseClass = new StringBuilder(IMenuButton.BASE_STYLE_CLASS);
        StringBuilder buttonClass = new StringBuilder(IMenuButton.BUTTON_STYLE_CLASS) ;
        StringBuilder selectClass = new StringBuilder(IMenuButton.MENU_SELECT_CLASS);
        String userDefinedClass = menu.getStyleClass();
        if (null != userDefinedClass) {
            baseClass.append(userDefinedClass);
            selectClass.append(userDefinedClass);
            buttonClass.append(userDefinedClass);
        }
        // apply disabled style state if specified.
        if (menu.isDisabled()) {
            baseClass.append(IMenuButton.DISABLED_STYLE_CLASS);
        }
        writer.writeAttribute(CLASS_ATTR, baseClass.toString());

         // should be auto base though
        writer.startElement(SPAN_ELEM, menu);
        writer.writeAttribute(CLASS_ATTR, buttonClass.toString());
        writer.writeAttribute(ID_ATTR, clientId+"_btn");
        writer.startElement(SPAN_ELEM, menu);
        String selectLabel = menu.getButtonLabel();
        writer.write(selectLabel);
        writer.endElement(SPAN_ELEM);
        writer.endElement(SPAN_ELEM);
        if (menu.isDisabled()) {
            writer.writeAttribute(DISABLED_ATTR, "disabled");  //what about disabled class?
            writer.endElement(DIV_ELEM);
            return;
        }
        writer.startElement(SELECT_ELEM, menu);
        writer.writeAttribute(ID_ATTR, clientId+"_sel");
        writer.writeAttribute(NAME_ATTR, clientId+"_sel");
        writer.writeAttribute(CLASS_ATTR, selectClass);
        writer.writeAttribute(ONCHANGE_ATTR, "mobi.menubutton.select('"+clientId+"');");
        if (null!=menu.getStyle()){
            String style= menu.getStyle();
            if ( style.trim().length() > 0) {
                writer.writeAttribute(STYLE_ATTR, style);
            }
        }
    }

    public void encodeEnd(IMenuButton button, IResponseWriter writer) throws IOException {
        writer.endElement(SELECT_ELEM);
        writer.endElement(DIV_ELEM);
    }


}
