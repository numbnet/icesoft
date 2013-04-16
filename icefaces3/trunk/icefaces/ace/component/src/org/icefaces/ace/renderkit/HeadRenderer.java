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

import org.icefaces.ace.component.themeselect.ThemeSelect;
import org.icefaces.ace.util.Constants;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.*;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.ListIterator;
import java.util.Map;

@ListenerFor(systemEventClass=PreRenderComponentEvent.class)
public class HeadRenderer extends Renderer implements ComponentSystemEventListener {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("head", component);

        //Resources
        UIViewRoot viewRoot = context.getViewRoot();
        ListIterator<UIComponent> iter = (viewRoot.getComponentResources(context, "head")).listIterator();
		boolean developmentStage = context.isProjectStage(ProjectStage.Development);
        while (iter.hasNext()) {
            UIComponent resource = (UIComponent) iter.next();
			if (developmentStage) {
				resource = modifyIfAceScriptResource(resource);
			}
            resource.encodeAll(context);
        }
    }
	
	private UIComponent modifyIfAceScriptResource(UIComponent resource) {
		if (resource instanceof UIOutput) {
			if ("javax.faces.resource.Script".equals(resource.getRendererType())) {
				Map<String, Object> attrs = resource.getAttributes();
				Object library = attrs.get("library");
				Object nameValue = attrs.get("name");
				if ("icefaces.ace".equals(library)) {
					String name = nameValue == null ? "" : (String) nameValue;
					if (name.endsWith("ace-yui.js")) {
						return createAceScriptResource("util/ace-yui.uncompressed.js");
					} else if (name.endsWith("ace-jquery.js")) {
						return createAceScriptResource("util/ace-jquery.uncompressed.js");
					} else if (name.endsWith("ace-datatable.js")) {
						return createAceScriptResource("util/ace-datatable.uncompressed.js");
					} else if (name.endsWith("ace-menu.js")) {
						return createAceScriptResource("util/ace-menu.uncompressed.js");
					} else if (name.endsWith("ace-components.js")) {
						return createAceScriptResource("util/ace-components.uncompressed.js");
					} else if (name.endsWith("ace-chart.js")) {
						return createAceScriptResource("chart/ace-chart.uncompressed.js");
					}
				}
			}
		}
		return resource;
	}
	
	private UIComponent createAceScriptResource(String name) {
        UIComponent resource = FacesContext.getCurrentInstance().getApplication().createComponent("javax.faces.Output");
        resource.setRendererType("javax.faces.resource.Script");

        Map<String, Object> attrs = resource.getAttributes();
        attrs.put("name", name);
        attrs.put("library", "icefaces.ace");

        return resource;	
	}

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        //no-op
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.endElement("head");
    }

    private UIComponent createThemeResource(FacesContext fc, String library, String resourceName) {
        UIComponent resource = fc.getApplication().createComponent("javax.faces.Output");
        resource.setRendererType("javax.faces.resource.Stylesheet");
        resource.setTransient(true);

        Map<String, Object> attrs = resource.getAttributes();
        attrs.put("name", resourceName);
        attrs.put("library", library);
        attrs.put("target", "head");

        return resource;
    }

    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        String theme = (String) context.getExternalContext().getSessionMap().get(Constants.THEME_PARAM);
        if (theme == null) {
            theme = "";
        } else {
            theme = theme.trim();
        }
        String name;
        String library;

        if ("".equals(theme) || theme.equalsIgnoreCase("sam")) {
            library = "icefaces.ace";
            name = "themes/sam/theme.css";
        } else if (theme.equalsIgnoreCase("rime")) {
            library = "icefaces.ace";
            name = "themes/rime/theme.css";
        } else {
            library = "ace-" + theme;
            name = "theme.css";
        }

        UIComponent resource = createThemeResource(context, library, name);
        context.getViewRoot().addComponentResource(context, resource);
    }
}
