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
import org.icemobile.util.CSSUtils;

import java.io.IOException;
import java.util.logging.Logger;

import static org.icemobile.util.HTML.*;

public class SubmitNotificationCoreRenderer extends BaseCoreRenderer {
    private static final Logger logger =
            Logger.getLogger(SubmitNotificationCoreRenderer.class.toString());

    public void encodeBegin(ISubmitNotification panel, IResponseWriter writer)
            throws IOException{
        String clientId = panel.getClientId();
        StringBuilder popupBaseClass = new StringBuilder(ISubmitNotification.CONTAINER_HIDE_CLASS);
        // div that is use to hide/show the popup screen black out--will manipulate using js
        writer.startElement(DIV_ELEM, panel);
        writer.writeAttribute(ID_ATTR, clientId + "_bg");
        writer.writeAttribute(CLASS_ATTR, ISubmitNotification.BLACKOUT_PNL_HIDE_CLASS);
        writer.endElement(DIV_ELEM);
        // panel
        writer.startElement(DIV_ELEM, panel);
        if (null != panel.getStyleClass())  {
            popupBaseClass.append(" ").append(panel.getStyleClass());
        }
        writer.writeAttribute(ID_ATTR, clientId + "_popup");
        writer.writeAttribute(CLASS_ATTR, popupBaseClass.toString());
        if (null != panel.getStyle()){
            writer.writeAttribute(STYLE_ATTR, panel.getStyle());
        }
        writer.startElement(DIV_ELEM, panel);
        writer.writeAttribute(ID_ATTR, clientId + "_popup_inner");
    }

    public void encodeEnd(ISubmitNotification panel, IResponseWriter writer) throws IOException {
        writer.endElement(DIV_ELEM);
        writer.endElement(DIV_ELEM);
    }


}
