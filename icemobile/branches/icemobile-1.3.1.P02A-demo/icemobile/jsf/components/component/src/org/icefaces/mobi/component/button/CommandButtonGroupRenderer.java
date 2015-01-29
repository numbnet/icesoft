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

package org.icefaces.mobi.component.button;

import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icemobile.component.IButtonGroup;
import org.icemobile.renderkit.ButtonGroupCoreRenderer;
import org.icemobile.renderkit.IResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * CommandButtonGroup is a simple container for command buttons to format them
 * in a horizontal or vertical fashion.  The component renders a div element
 * with appropriate style classes.  If a mobi:commandButton component's should
 * only be inserted at child elements but no explicit checking is made.
 */
public class CommandButtonGroupRenderer extends Renderer {
    private static final Logger logger = Logger.getLogger(CommandButtonGroupRenderer.class.getName());

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        IButtonGroup buttonGroup = (IButtonGroup)uiComponent;
        IResponseWriter writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        ButtonGroupCoreRenderer renderer = new ButtonGroupCoreRenderer();
        renderer.encodeBegin(buttonGroup, writer);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        IButtonGroup buttonGroup = (IButtonGroup)uiComponent;
        IResponseWriter writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        ButtonGroupCoreRenderer renderer = new ButtonGroupCoreRenderer();
        renderer.encodeEnd(buttonGroup, writer);
    }
}
