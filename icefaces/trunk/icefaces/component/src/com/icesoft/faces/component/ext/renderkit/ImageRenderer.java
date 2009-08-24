/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */

package com.icesoft.faces.component.ext.renderkit;

import com.icesoft.faces.context.ByteArrayResource;
import com.icesoft.faces.context.FileResource;
import com.icesoft.faces.context.Resource;
import com.icesoft.faces.context.ResourceRegistry;

import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Map;

public class ImageRenderer
        extends com.icesoft.faces.renderkit.dom_html_basic.ImageRenderer {
    private static Log log = LogFactory.getLog(ImageRenderer.class);
    protected String processSrcAttribute(FacesContext facesContext, UIGraphic uiGraphic) {
        Object o = uiGraphic.getValue();
        if (o == null) {
            o = uiGraphic.getUrl();
        }
        if (o == null) {
            log.error("The value of graphicImage component is missing", new NullPointerException());
            return new String();
        }
        if (o instanceof byte[]) {
            final Map attributes = uiGraphic.getAttributes();
            final String mimeType = attributes.containsKey("mimeType") ? String.valueOf(attributes.get("mimeType")) : "";
            ByteArrayResource bar = new ByteArrayResource((byte[]) o) {
                public void withOptions(Options options) throws IOException {
                    super.withOptions(options);
                    options.setMimeType(mimeType);
                }
            };
            return (((ResourceRegistry) facesContext).registerResource(bar)).getPath();
        } else if (o instanceof Resource) {
            return (((ResourceRegistry) facesContext).registerResource((Resource)o)).getPath();
        } else {
            // delegate to the parent class
            return super.processSrcAttribute(facesContext, uiGraphic);
        }
    }
}
