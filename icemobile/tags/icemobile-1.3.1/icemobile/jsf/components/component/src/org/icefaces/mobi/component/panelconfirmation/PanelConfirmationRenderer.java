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
package org.icefaces.mobi.component.panelconfirmation;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.JSFUtils;
import org.icemobile.component.IPanelConfirmation;
import org.icemobile.renderkit.IResponseWriter;
import org.icemobile.renderkit.PanelConfirmationCoreRenderer;
import org.icemobile.util.CSSUtils;

/**
 * for now the css for this class is just reused from the dateSpinner popup container classes
 */
public class  PanelConfirmationRenderer extends BaseLayoutRenderer {
    private static final Logger logger = Logger.getLogger(PanelConfirmationRenderer.class.getName());
    private static final String JS_NAME = "panelconfirmation.js";
    private static final String JS_MIN_NAME = "panelconfirmation-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.panelconfirmation";


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        IPanelConfirmation panel = (IPanelConfirmation) uiComponent;
        IResponseWriter writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        String clientId = panel.getClientId();
        writeJavascriptFile(facesContext, uiComponent, JS_NAME, JS_MIN_NAME, JS_LIBRARY);
        PanelConfirmationCoreRenderer renderer = new PanelConfirmationCoreRenderer();
        renderer.encodeEnd(panel, writer);
    }
}
