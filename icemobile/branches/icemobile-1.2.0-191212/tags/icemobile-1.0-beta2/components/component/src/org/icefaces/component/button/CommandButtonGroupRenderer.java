/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icefaces.component.button;

import org.icefaces.component.utils.HTML;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * CommandButtonGroup is a simple container for command buttons to format them
 * in a horizontal or vertical fashion.  The component renders a div element
 * with appropriate style classes.  If a mobi:commandButton component's should
 * only be inserted at child elements but no explicit checking is made.
 */
public class CommandButtonGroupRenderer extends Renderer {
    private static Logger logger = Logger.getLogger(CommandButtonGroupRenderer.class.getName());

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        CommandButtonGroup buttonGroup = (CommandButtonGroup) uiComponent;

        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);

        // apply the default style classes
        String orientation = buttonGroup.getOrientation();
        StringBuilder styleClasses = new StringBuilder(CommandButtonGroup.DEFAULT_STYLE_CLASS);
        // assign orientation style
        if (CommandButtonGroup.ORIENTATION_VERTICAL.equals(orientation)) {
            styleClasses.append(CommandButtonGroup.VERTICAL_STYLE_CLASS);
        }else{
            styleClasses.append(CommandButtonGroup.HORIZONTAL_STYLE_CLASS);
        }
        // finally assign any user defines style
        String userDefinedClass = buttonGroup.getStyleClass();
        if (userDefinedClass != null && !userDefinedClass.isEmpty()){
            styleClasses.append(" ").append(userDefinedClass);
        }
        writer.writeAttribute(HTML.CLASS_ATTR, styleClasses, "styleClass");
        // append any users specified style info
        writer.writeAttribute(HTML.STYLE_ATTR, buttonGroup.getStyle(), "style");
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HTML.DIV_ELEM);
    }
}
