/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */
package org.icefaces.ace.renderkit;

import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.render.Renderer;
import java.util.ListIterator;
import java.util.Map;

public class UncompressAceResources implements SystemEventListener {

    public boolean isListenerForSource(Object source) {
        return source instanceof UIViewRoot;
    }

    public void processEvent(SystemEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        //Resources
        UIViewRoot viewRoot = context.getViewRoot();
        ListIterator<UIComponent> iter = (viewRoot.getComponentResources(context, "head")).listIterator();
        boolean developmentStage = context.isProjectStage(ProjectStage.Development);
        if (developmentStage) {
            while (iter.hasNext()) {
                UIComponent resource = (UIComponent) iter.next();
                switchToUncompressedResource(resource);
            }
        }
    }

    private static void switchToUncompressedResource(UIComponent resource) {
        if (resource instanceof UIOutput) {
            if ("javax.faces.resource.Script".equals(resource.getRendererType())) {
                Map<String, Object> attrs = resource.getAttributes();
                Object library = attrs.get("library");
                Object nameValue = attrs.get("name");
                if ("icefaces.ace".equals(library)) {
                    String name = nameValue == null ? "" : (String) nameValue;
                    if (name.endsWith("ace-yui.js")) {
                        attrs.put("name", "util/ace-yui.uncompressed.js");
                    } else if (name.endsWith("ace-jquery.js")) {
                        attrs.put("name", "util/ace-jquery.uncompressed.js");
                    } else if (name.endsWith("ace-datatable.js")) {
                        attrs.put("name", "util/ace-datatable.uncompressed.js");
                    } else if (name.endsWith("ace-menu.js")) {
                        attrs.put("name", "util/ace-menu.uncompressed.js");
                    } else if (name.endsWith("ace-components.js")) {
                        attrs.put("name", "util/ace-components.uncompressed.js");
                    } else if (name.endsWith("ace-chart.js")) {
                        attrs.put("name", "chart/ace-chart.uncompressed.js");
                    }
                }
            }
        }
    }
}
