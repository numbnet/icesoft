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

package org.icemobile.renderkit;


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
        writer.writeAttribute("data-lastselected", menu.getLastSelected());
        if (null!=menu.getStyle()){
            String style= menu.getStyle();
            if ( style.trim().length() > 0) {
                writer.writeAttribute(STYLE_ATTR, style);
            }
        }
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
        }
        writer.startElement(SELECT_ELEM, menu);
        writer.writeAttribute(ID_ATTR, clientId+"_sel");
        if (null == menu.getName()){
            writer.writeAttribute(NAME_ATTR, clientId+"_sel");
        } else {
            writer.writeAttribute(NAME_ATTR, menu.getName());
        }
        writer.writeAttribute(CLASS_ATTR, selectClass);
        if (!menu.isDisabled()){
            writer.writeAttribute(ONCHANGE_ATTR, "ice.mobi.menubutton.select('"+clientId+"');");
        }

    }

    public void encodeEnd(IMenuButton button, IResponseWriter writer) throws IOException {
        writer.endElement(SELECT_ELEM);
   //     encodeInitScript(button, writer);
        writer.endElement(DIV_ELEM);
    }

    private void encodeInitScript(IMenuButton button, IResponseWriter writer) throws IOException{
        String clientId = button.getClientId();
        writer.startElement(SPAN_ELEM, button);
        writer.writeAttribute(ID_ATTR, clientId+"_initScr");
        writer.writeAttribute(CLASS_ATTR, "mobi-hidden");
        writer.startElement(SCRIPT_ELEM, button);
        writer.writeAttribute(TYPE_ATTR, "text/javascript");
        StringBuilder sb = new StringBuilder("ice.mobi.menubutton.initmenu('");
        sb.append(clientId).append("'");
        if (null != button.getSelectTitle()) {
            sb.append(",").append("{ selectTitle: '").append(button.getSelectTitle()).append("'});");
        } else {
            sb.append(");") ;
        }
        writer.write(sb.toString());
        writer.endElement(SCRIPT_ELEM);
        writer.endElement(SPAN_ELEM);
     }
    public void encodeSelectTitle(IMenuButton item, IResponseWriter writer)
            throws IOException{
        writer.startElement(OPTION_ELEM, item);
        // ask Philip about styleClass and style.
        writer.writeAttribute(VALUE_ATTR, "");
        writer.write(item.getSelectTitle());
        writer.endElement(OPTION_ELEM);
    }
}
