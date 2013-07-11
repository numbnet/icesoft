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
package org.icefaces.mobi.component.openresource;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.mobi.renderkit.BaseResourceRenderer;
import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icefaces.mobi.utils.PassThruAttributeWriter;
import org.icemobile.renderkit.OpenResourceCoreRenderer;
import org.icemobile.renderkit.IResponseWriter;
import org.icemobile.component.IOpenResource;
import org.icemobile.util.ClientDescriptor;


public class OpenResourceRenderer extends BaseResourceRenderer {
    private static final Logger log = Logger.getLogger(OpenResourceRenderer.class.getName());


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        IResponseWriter writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        OpenResource comp = (OpenResource) uiComponent;
        String clientId = comp.getClientId();
        Object compObject = comp.getValue();
        String scope = comp.getScope().toLowerCase().trim();
        if (!scope.equals("flash") && !(scope.equals("window")) && !(scope.equals("application"))
                && (!scope.equals("request")) && (!scope.equals("view"))) {
            scope = "session";
        }
        String name = comp.getName();
        if (null == name || name.equals("")) name = "openresource" + clientId;

        String srcAttribute = null;
        if (null != compObject) {
            srcAttribute = processSrcAttribute(facesContext, compObject, name, comp.getMimeType(),
                    scope, compObject.toString());
            if (srcAttribute!=null){
                comp.setSrcAttribute(srcAttribute);
            }
        }
        //IOpenResource icomp = IOpenResource(uiComponent);
        OpenResourceCoreRenderer renderer = new OpenResourceCoreRenderer();
        renderer.encodeEnd(comp, writer);
    }


}
