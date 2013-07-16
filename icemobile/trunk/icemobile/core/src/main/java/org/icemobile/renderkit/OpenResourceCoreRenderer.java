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

import org.icemobile.component.IOpenResource;
import org.icemobile.util.ClientDescriptor;

import java.io.IOException;
import java.util.logging.Logger;

import static org.icemobile.util.HTML.*;

public class OpenResourceCoreRenderer extends BaseCoreRenderer{
    public static final String BASE_CLASS = "mobi-openresource";
    private static final Logger logger =
            Logger.getLogger(OpenResourceCoreRenderer.class.toString());

    public void encodeEnd(IOpenResource comp, IResponseWriter writer)
            throws IOException {

        String clientId = comp.getClientId();
        ClientDescriptor client = comp.getClient();

        writer.startElement(SPAN_ELEM, comp);
        String cssclass = BASE_CLASS;
        if (!isStringAttributeEmpty(comp.getStyleClass())){
            cssclass+= " "+comp.getStyleClass();
        }
        writer.writeAttribute(CLASS_ATTR, cssclass);
        if (!isStringAttributeEmpty(comp.getStyle())){
            writer.writeAttribute(STYLE_ELEM, comp.getStyle());
        }    //want style on surrounding div or anchor element?
        writer.startElement(ANCHOR_ELEM, comp);
        writer.writeAttribute(ID_ATTR, clientId);
        String name = clientId;
        writer.writeAttribute("data-id", comp.getClientId());
//        if (null != comp.getSessionId())  {
//            writer.writeAttribute("data-jsessionid", comp.getSessionId());
//        }
        writer.writeAttribute("data-command", "open");
        writer.writeAttribute("data-ub", ".");
        writer.writeAttribute("target", "_blank");
        String srcAttribute = "none"; //not sure what you want for a null value?
        //does target need to be a component attribute?
        if (!isStringAttributeEmpty(comp.getSrcAttribute())){
            srcAttribute = comp.getSrcAttribute();
        }
        writer.writeAttribute("data-params", "url=" + srcAttribute);
        writer.writeAttribute(ONCLICK_ATTR, "ice.mobi.invoke(this)");
//        writer.writeAttribute(HREF_ATTR, srcAttribute);
        String label = "openResourceLabel";
        if (!isStringAttributeEmpty(comp.getLabel())){
            label = comp.getLabel();
        }
        writer.writeText(label);
        writer.endElement(ANCHOR_ELEM);

        writer.endElement(SPAN_ELEM);
    }

}
