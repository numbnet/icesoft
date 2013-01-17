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

package com.icesoft.faces.component.ext.renderkit;

import com.icesoft.faces.context.ByteArrayResource;
import com.icesoft.faces.context.ResourceRegistryLocator;

import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

public class ImageRenderer
        extends com.icesoft.faces.renderkit.dom_html_basic.ImageRenderer {

    protected String processSrcAttribute(FacesContext facesContext, UIGraphic uiGraphic) {
        Object o = uiGraphic.getValue();
        if (o instanceof byte[]) {
            final Map attributes = uiGraphic.getAttributes();
            final String mimeType = attributes.containsKey("mimeType") ? String.valueOf(attributes.get("mimeType")) : "";
            ByteArrayResource bar = new ByteArrayResource((byte[]) o) {
                public void withOptions(Options options) throws IOException {
                    super.withOptions(options);
                    options.setMimeType(mimeType);
                }
            };

            return ResourceRegistryLocator.locate(facesContext).registerResource(bar).getPath();
        } else {
            // delegate to the parent class
            return super.processSrcAttribute(facesContext, uiGraphic);
        }
    }
}
