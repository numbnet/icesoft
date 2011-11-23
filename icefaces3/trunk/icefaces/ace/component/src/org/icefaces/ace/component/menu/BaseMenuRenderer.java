/*
 * Original Code developed and contributed by Prime Technology.
 * Subsequent Code Modifications Copyright 2011 ICEsoft Technologies Canada Corp. (c)
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
package org.icefaces.ace.component.menu;

import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.icefaces.ace.component.menuitem.MenuItem;
import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.Utils;
import org.icefaces.impl.event.AjaxDisabledList;

import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import java.util.*;
import org.icefaces.ace.component.ajax.AjaxBehavior;

public abstract class BaseMenuRenderer extends CoreRenderer {

    @Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		AbstractMenu menu = (AbstractMenu) component;

		if(menu.shouldBuildFromModel()) {
			menu.buildMenuFromModel();
		}

		encodeMarkup(context, menu);
		encodeScript(context, menu);
	}

    protected abstract void encodeMarkup(FacesContext context, AbstractMenu abstractMenu) throws IOException;

    protected abstract void encodeScript(FacesContext context, AbstractMenu abstractMenu) throws IOException;

    protected void encodeMenuItem(FacesContext context, MenuItem menuItem) throws IOException {
		String clientId = menuItem.getClientId(context);
        ResponseWriter writer = context.getResponseWriter();
        String icon = menuItem.getIcon();

		if(menuItem.shouldRenderChildren()) {
			renderChildren(context, menuItem);
		}
        else {
            writer.startElement("a", null);

			if(menuItem.getUrl() != null) {
				writer.writeAttribute("href", getResourceURL(context, menuItem.getUrl()), null);
				if(menuItem.getOnclick() != null) writer.writeAttribute("onclick", menuItem.getOnclick(), null);
				if(menuItem.getTarget() != null) writer.writeAttribute("target", menuItem.getTarget(), null);
			} else {
				writer.writeAttribute("href", "#", null);

				UIComponent form = ComponentUtils.findParentForm(context, menuItem);
				if(form == null) {
					throw new FacesException("Menubar must be inside a form element");
				}

				String formClientId = form.getClientId(context);
				String command;
				
				boolean hasAjaxBehavior = false;
				
				StringBuilder behaviors = new StringBuilder();
				behaviors.append("this.id = '" + clientId + "'; (function() { "); // dynamically set the id to the node so that it can be handled by the submit functions 
				// ClientBehaviors
				Map<String,List<ClientBehavior>> behaviorEvents = menuItem.getClientBehaviors();
				if(!behaviorEvents.isEmpty()) {
					List<ClientBehaviorContext.Parameter> params = Collections.emptyList();
					for(Iterator<ClientBehavior> behaviorIter = behaviorEvents.get("click").iterator(); behaviorIter.hasNext();) {
						ClientBehavior behavior = behaviorIter.next();
						if (behavior instanceof AjaxBehavior)
							hasAjaxBehavior = true;
						ClientBehaviorContext cbc = ClientBehaviorContext.createClientBehaviorContext(context, menuItem, "click", clientId, params);
						String script = behavior.getScript(cbc);    //could be null if disabled

						if(script != null) {
							behaviors.append(script);
							behaviors.append(";");
						}
					}
				}
				behaviors.append(" })(null, {'ice.customUpdate':'" + clientId +"'});");
				command = behaviors.toString();
				
                if (!hasAjaxBehavior) {
                    command += buildNonAjaxRequest(context, menuItem, formClientId, clientId);
                    UIForm theForm = AjaxDisabledList.getContainingForm(menuItem);
                    if (null != theForm)  {
                        String disabledList = (String) theForm.getAttributes()
                            .get(AjaxDisabledList.DISABLED_LIST);
                        if (null == disabledList)  {
                            disabledList = " ";
                        }
                        disabledList = disabledList + clientId + " ";
                        theForm.getAttributes().put(AjaxDisabledList.DISABLED_LIST, disabledList);
                    }
                }

				command = menuItem.getOnclick() == null ? command : menuItem.getOnclick() + ";" + command;

				writer.writeAttribute("onclick", command, null);
			}

            if(icon != null) {
                writer.startElement("span", null);
                writer.writeAttribute("class", icon + " wijmo-wijmenu-icon-left", null);
                writer.endElement("span");
            }

			if(menuItem.getValue() != null) {
                writer.startElement("span", null);
                String style = menuItem.getStyle();
                if (style != null && style.trim().length() > 0) {
                    writer.writeAttribute("style", style, "style");
                }
                Utils.writeConcatenatedStyleClasses(writer, "wijmo-wijmenu-text", menuItem.getStyleClass());
                writer.write((String) menuItem.getValue());
                writer.endElement("span");
            }

            writer.endElement("a");
		}
	}

    @Override
	public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
		//Do nothing
	}

    @Override
	public boolean getRendersChildren() {
		return true;
	}
}
