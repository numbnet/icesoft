/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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

package org.icefaces.mobi.renderkit;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;



public class BaseLayoutRenderer extends CoreRenderer {
    /*
        in order to use these must have empty encodeChildren method in Renderer.
     */
    protected void renderChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        for (UIComponent child : uiComponent.getChildren()) {
            renderChild(facesContext, child);
        }
    }

    protected void renderChild(FacesContext facesContext, UIComponent child) throws IOException {
        if (!child.isRendered()) {
            return;
        }
        //do we have to worry about encodeAll method???
        child.encodeBegin(facesContext);
        if (child.getRendersChildren()) {
            child.encodeChildren(facesContext);
        } else {
            renderChildren(facesContext, child);
        }
        child.encodeEnd(facesContext);
    }
    /**
     * used by content pane controllers...implement ContentPaneController
     * @param context
     * @param uiComponent
     * @throws IOException
     */
    protected void encodeHidden(FacesContext context, UIComponent uiComponent) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = uiComponent.getClientId(context);
        writer.startElement("span", uiComponent);
        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "hidden", "type");
        writer.writeAttribute("id", clientId+"_hidden", "id");
        writer.writeAttribute("name", clientId+"_hidden", "name");
        writer.endElement("input");
        writer.endElement("span");
    }

    /**
     * used by classes which implement ContentPaneController
     * @param context
     * @param component
     * @param oldIndex
     * @param id
     * @return
     */
     protected int findIndex(FacesContext context, UIComponent component, int oldIndex, String id){
        for (int i=0; i < component.getChildCount();i++){
            if (component.getChildren().get(i).getClientId(context).equals(id)){
                  return i;
            }
        }
        return oldIndex;
    }
}
