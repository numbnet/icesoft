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
package org.icefaces.mobi.component.augmentedreality;


import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.Utils;

import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;

public class AugmentedRealityLocationsRenderer extends BaseLayoutRenderer {
    private static Logger logger = Logger.getLogger(AugmentedRealityLocationsRenderer.class.getName());

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
    Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    AugmentedRealityLocations item = (AugmentedRealityLocations) uiComponent;
    String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
    String clientId = item.getClientId();
    String parentId = item.getParent().getClientId();
    if (clientId.equals(source) || parentId.equals(source)) {
            try {
                if (!item.isDisabled()) {
                    if (requestParameterMap.containsKey(clientId)) {
                        String submittedString = String.valueOf(requestParameterMap.get(clientId));
                        if (submittedString==null) {
                            return;
                        } else {
                           logger.info("submittedString="+submittedString);
                           item.setSelectedLocationItem(submittedString);
                        }
                    }
                }
            } catch (Exception e) {
                logger.warning("Error queuing CommandButton event");
            }
    }

 }

 public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
          throws IOException {
     ResponseWriter writer = facesContext.getResponseWriter();
     AugmentedRealityLocations list = (AugmentedRealityLocations)uiComponent;
     String clientId = uiComponent.getClientId(facesContext);
     boolean disabled = list.isDisabled();
     String parentId = uiComponent.getParent().getClientId();
     UIComponent parent = uiComponent.getParent();
/*     if (!(parent instanceof AugmentedReality)){
         logger.warning("This component must have parent tag of augmentedReality");
         return;
     }   */
//     AugmentedRealityLocations parentComp = (AugmentedRealityLocations)parent;
     writer.startElement(HTML.SELECT_ELEM, uiComponent);
     writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
     writer.writeAttribute(HTML.NAME_ATTR, clientId, HTML.NAME_ATTR);
     StringBuilder sb = new StringBuilder("ice.se(null, '"+clientId+"');");
     writer.writeAttribute(HTML.ONCHANGE_ATTR,sb.toString() , HTML.ONCHANGE_ATTR);
     String userDefinedClass = list.getStyleClass();
     /*    String styleClass = AugmentedReality.LOCATIONLIST_CLASS;
           if (userDefinedClass != null) {
                    styleClass += " " + userDefinedClass;
     }
     writer.writeAttribute("class", styleClass, "styleClass");  */
     if (list.getVar() != null) {
         list.setRowIndex(-1);
         for (int i = 0; i < list.getRowCount(); i++) {
             //assume that if it's a list of items then it's grouped
             list.setRowIndex(i);
             renderChildren(facesContext, list);
         }
         list.setRowIndex(-1);

     }else {
             //doing it with indiv location item tag's
        renderChildren(facesContext, list);
     }
     writer.endElement(HTML.SELECT_ELEM);
 }
    @Override
    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
         //Rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

}
