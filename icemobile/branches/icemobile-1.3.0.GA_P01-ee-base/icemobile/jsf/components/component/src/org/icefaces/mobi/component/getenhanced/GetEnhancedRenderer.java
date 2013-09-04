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

package org.icefaces.mobi.component.getenhanced;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.icefaces.mobi.renderkit.CoreRenderer;
import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icemobile.renderkit.GetEnhancedCoreRenderer;


public class GetEnhancedRenderer extends CoreRenderer {
    private static final Logger log = Logger.getLogger(GetEnhancedRenderer.class.getName());
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriterWrapper writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        GetEnhanced getEnhanced = (GetEnhanced) uiComponent;
        GetEnhancedCoreRenderer renderer = new GetEnhancedCoreRenderer();
        renderer.encode(getEnhanced, writer);        
    }


}
