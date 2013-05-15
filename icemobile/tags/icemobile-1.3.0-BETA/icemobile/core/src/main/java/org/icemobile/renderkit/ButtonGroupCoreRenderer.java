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

import org.icemobile.component.IButtonGroup;

import java.io.IOException;
import java.util.logging.Logger;

import static org.icemobile.util.HTML.*;

public class ButtonGroupCoreRenderer extends BaseCoreRenderer {
    private static final Logger logger =
            Logger.getLogger(ButtonGroupCoreRenderer.class.toString());

    public void encodeBegin(IButtonGroup component, IResponseWriter writer)
            throws IOException {
        IButtonGroup buttonGroup = (IButtonGroup) component;
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(ID_ATTR, buttonGroup.getClientId());
        writer.writeAttribute(NAME_ATTR, buttonGroup.getClientId());
        // apply the default style classes
        String orientation = buttonGroup.getOrientation();
        StringBuilder styleClasses = new StringBuilder(IButtonGroup.DEFAULT_STYLE_CLASS);
        // assign orientation style
        if (IButtonGroup.ORIENTATION_VERTICAL.equals(orientation)) {
            styleClasses.append(IButtonGroup.VERTICAL_STYLE_CLASS);
        }else{
            styleClasses.append(IButtonGroup.HORIZONTAL_STYLE_CLASS);
        }
        // finally assign any user defines style
        String userDefinedClass = buttonGroup.getStyleClass();
        if (userDefinedClass != null && userDefinedClass.length() > 0){
            styleClasses.append(" ").append(userDefinedClass);
        }
        writer.writeAttribute(CLASS_ATTR, styleClasses);
        // append any users specified style info
        writer.writeAttribute(STYLE_ATTR, buttonGroup.getStyle());
        writer.startElement(DIV_ELEM);
    }
    public void encodeEnd(IButtonGroup component, IResponseWriter writer)
            throws IOException {
        //don't need hidden for jsf
        writer.endElement(DIV_ELEM);
        if (component.getName() !=null){
            encodeHiddenSelected(writer, component.getClientId(), component.getSelectedId(), component.getName());
        }
        writer.endElement(DIV_ELEM);
    }


}
