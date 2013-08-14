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
package org.icefaces.mobi.component.viewmanager;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.JSFUtils;

public class ViewRenderer extends BaseLayoutRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {

    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
       View view = (View)uiComponent;
       validate(view);
       ResponseWriter writer = facesContext.getResponseWriter();
       writer.startElement(HTML.DIV_ELEM, view);
       writer.writeAttribute(HTML.ID_ATTR, view.getId(), null);
       writer.writeAttribute(HTML.CLASS_ATTR, "mobi-vm-view", null);
       String title = view.getTitle();
       if( title != null ){
    	   writer.writeAttribute("data-title", title, null);
       }
       
    }

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        JSFUtils.renderChildren(facesContext, uiComponent);
    }


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HTML.DIV_ELEM);
    }
    
    private void validate(View view){
       if( view.getParent() instanceof ViewManager ){
    	   throw new IllegalStateException("The view component must be a child of the viewManager component");
       }
    }
}
