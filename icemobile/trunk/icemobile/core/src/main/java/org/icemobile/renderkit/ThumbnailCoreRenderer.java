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

import java.io.IOException;

import org.icemobile.component.IThumbnail;
import org.icemobile.util.ClientDescriptor;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.icemobile.util.HTML.*;

public class ThumbnailCoreRenderer {
    private static final Logger logger =
            Logger.getLogger(ThumbnailCoreRenderer.class.toString());
    public void encode(IThumbnail component, IResponseWriter writer)
            throws IOException {

        String clientId = component.getClientId();
        ClientDescriptor cd = component.getClient();
        if (cd.isDesktopBrowser()){
        //    logger.info("desktop browser");
            return;
        }
        boolean renderThumbnail = false;
        if (cd.isICEmobileContainer() || cd.isSXRegistered()){
            renderThumbnail = true;
        }
        if (!renderThumbnail){
            return;
        }
        String thumbId = component.getMFor() + "-thumb";
        writer.startElement(SPAN_ELEM, component);
        String styleClass = component.getBaseClass();
        if( component.getStyleClass() != null ){
            styleClass += " " + component.getStyleClass();
        }
        writer.writeAttribute(CLASS_ATTR, styleClass);
        String style = component.getStyle();
        if( style != null ){
            writer.writeAttribute(STYLE_ATTR, style);
        }
        writer.startElement(IMG_ELEM, component);
        writer.writeAttribute(WIDTH_ATTR, "64");
        writer.writeAttribute(HEIGHT_ATTR, "64");
        writer.writeAttribute(ID_ATTR, thumbId);
        writer.endElement(IMG_ELEM);
        writer.endElement(SPAN_ELEM);
    }

}
