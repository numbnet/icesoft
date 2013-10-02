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

import org.icemobile.component.IContentPane;
import org.icemobile.util.ClientDescriptor;
import org.icemobile.util.Utils;
import org.icemobile.util.CSSUtils;


import java.io.IOException;
import java.util.logging.Logger;

import static org.icemobile.util.HTML.*;

public class ContentPaneCoreRenderer extends BaseCoreRenderer {
    /* see AccordionPaneCoreRenderer and TabPaneCoreRenderer as well for JSF */
    private static final Logger logger =
            Logger.getLogger(ContentPaneCoreRenderer.class.toString());
    
    public void encodeBegin(IContentPane pane, IResponseWriter writer,
                             boolean selected)
            throws IOException{
        String clientId = pane.getClientId();
        writer.startElement(DIV_ELEM, pane);
        writer.writeAttribute(ID_ATTR, clientId+"_wrp");
        writer.writeAttribute(CLASS_ATTR, selected ? IContentPane.CONTENT_BASE_CLASS : IContentPane.CONTENT_HIDDEN_CLASS);

        writer.startElement(DIV_ELEM, pane);
        writer.writeAttribute(ID_ATTR, clientId);
        if (pane.getStyleClass()!=null){
            writer.writeAttribute(CLASS_ATTR, pane.getStyleClass());
        }
        if (pane.getStyle()!=null){
            writer.writeAttribute(STYLE_ATTR, pane.getStyle());
        }
    }

    public void encodeEnd(IContentPane component, IResponseWriter writer)
             throws IOException {
        writer.endElement(DIV_ELEM);
        writer.endElement(DIV_ELEM);
    }

}
