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

package org.icefaces.ace.component.autocompleteentry;

import org.icefaces.ace.renderkit.InputRenderer;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.ace.util.JSONBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

import org.icefaces.impl.util.DOMUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

@MandatoryResourceComponent(tagName="autoCompleteEntry", value="org.icefaces.ace.component.autocompleteentry.autoCompleteEntry")
public class AutoCompleteEntryRenderer extends InputRenderer {

    private static final String AUTOCOMPLETE_DIV = "_div";
    static final String AUTOCOMPLETE_INDEX = "_idx";
	
    public boolean getRendersChildren() {
        return true;
    }
	
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        
		ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        AutoCompleteEntry autoCompleteEntry = (AutoCompleteEntry) uiComponent;
		
		// root
        writer.startElement("div", null);
		writer.writeAttribute("class", autoCompleteEntry.getStyleClass(), null);
		
		// text field
		writer.startElement("input", null);
        writer.writeAttribute("type", "text", null);
		//setRootElementId(facesContext, input, uiComponent);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("name", clientId, null);
		//writer.writeAttribute("class", autoCompleteEntry.getInputTextClass(), null);
		String mousedownScript = (String) uiComponent.getAttributes().get("onmousedown");
		mousedownScript = mousedownScript == null ? "" : mousedownScript;
		//input.setAttribute(HTML.ONMOUSEDOWN_ATTR, combinedPassThru(mousedownScript, "this.focus();"));
		writer.writeAttribute("onmousedown", mousedownScript + "this.focus();", null);
		int width = autoCompleteEntry.getWidth();
		writer.writeAttribute("style", "width: " + width + "px;", null);
		writer.writeAttribute("autocomplete", "off", null);
        String onfocusCombinedValue = "setFocus(this.id);";
        Object onfocusAppValue = uiComponent.getAttributes().get("onfocus");
        if (onfocusAppValue != null) {
            onfocusCombinedValue += onfocusAppValue.toString();
		}
        writer.writeAttribute("onfocus", onfocusCombinedValue, null);
        String onblurCombinedValue = "setFocus('');";
        Object onblurAppValue = uiComponent.getAttributes().get("onblur");
        if (onblurAppValue != null) {
            onblurCombinedValue += onblurAppValue.toString();
		}
        writer.writeAttribute("onblur", onblurCombinedValue, null);
        Object onchangeAppValue = uiComponent.getAttributes().get("onchange");
        if (onchangeAppValue != null) {
            writer.writeAttribute("onchange", onchangeAppValue.toString(), null);
		}
		// this would prevent, when first valueChangeListener fires with null value
		if (autoCompleteEntry.getValue() != null) {
			writer.writeAttribute("value", autoCompleteEntry.getValue(), null);
		}
		writer.endElement("input");
		
		// index
		writer.startElement("input", null);
		writer.writeAttribute("type", "hidden", null);
		String indexId = clientId + AUTOCOMPLETE_INDEX;
		writer.writeAttribute("name", indexId, null);
		writer.endElement("input");
		
		// div
		writer.startElement("div", null);
		String divId = clientId + AUTOCOMPLETE_DIV;
		writer.writeAttribute("id", clientId + AUTOCOMPLETE_DIV, null);
		//String listClass = autoCompleteEntry.getListClass(); // TODO: check for list class and use it instead
		writer.writeAttribute("style", "display:none;border:1px solid black;background-color:white;z-index:500;", null);
		writer.endElement("div");

		// script
		writer.startElement("script", null);
		writer.writeAttribute("id", clientId + "script", null);
		writer.writeAttribute("type", "text/javascript", null);
		boolean partialSubmit = false; // TODO: remove
		if (!autoCompleteEntry.isDisabled() && !autoCompleteEntry.isReadonly()) {
			writer.writeText("new ice.ace.Autocompleter('" + clientId + "','" + divId +
					"', " + "null" + " ,'" + "rowClass" + "','" + // TODO: remove rowClass, re-add autoCompleteEntry.getOptions()
					"selectedRowClass" + "'," + partialSubmit + ");", null); // TODO: remove selectedRowClass
		}
		writer.endElement("script");
		writer.endElement("div");
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        AutoCompleteEntry autoCompleteEntry = (AutoCompleteEntry) uiComponent;
		
		if (autoCompleteEntry.getValue() != null) {
            if (autoCompleteEntry.hasChanged()) {
                populateList(facesContext, autoCompleteEntry);
                autoCompleteEntry.setChangedComponentId(null);
            }
        } else {
			encodeDynamicScript(facesContext, autoCompleteEntry, "");
		}
    }


    public void populateList(FacesContext facesContext, AutoCompleteEntry autoCompleteEntry) throws IOException {
        autoCompleteEntry.populateItemList();
        Iterator matches = autoCompleteEntry.getItemList();
        int rows = autoCompleteEntry.getRows();
        if (rows == 0) rows = Integer.MAX_VALUE;
        int rowCounter = 0;
        if (autoCompleteEntry.getSelectFacet() != null) {
			/*
            UIComponent facet = autoCompleteEntry.getSelectFacet();

            Element listDiv = domContext.createElement(HTML.DIV_ELEM);
            Map requestMap =
                    facesContext.getExternalContext().getRequestMap();
            //set index to 0, so child components can get client id from autoComplete component
            autoCompleteEntry.setIndex(0);
            while (matches.hasNext() && rowCounter++ < rows) {

                Element div = domContext.createElement(HTML.DIV_ELEM);
                SelectItem item = (SelectItem) matches.next();
                requestMap.put(autoCompleteEntry.getListVar(), item.getValue());
                listDiv.appendChild(div);
                // When HTML is display we still need a selected value. Hidding the value in a hidden span
                // accomplishes this.
                Element spanToDisplay =
                        domContext.createElement(HTML.SPAN_ELEM);
                spanToDisplay.setAttribute(HTML.CLASS_ATTR, "informal");
                div.appendChild(spanToDisplay);
                domContext.setCursorParent(spanToDisplay);
                encodeParentAndChildren(facesContext, facet);
                Element spanToSelect =
                        domContext.createElement(HTML.SPAN_ELEM);
                spanToSelect.setAttribute(HTML.STYLE_ATTR,
                        "visibility:hidden;display:none;");
                String itemLabel = item.getLabel();
                if (itemLabel == null) {
                    itemLabel = converterGetAsString(
                            facesContext, autoCompleteEntry, item.getValue());
                }
                Text label = domContext.createTextNode((itemLabel));
                spanToSelect.appendChild(label);
                div.appendChild(spanToSelect);
                autoCompleteEntry.resetId(facet);
				
            }
            autoCompleteEntry.setIndex(-1);

            String nodeValue =
                    DOMUtils.nodeToString(listDiv).replaceAll("\n", "");
            String call = "Ice.Scriptaculous.Autocompleter.Finder.find('" +
                    autoCompleteEntry.getClientId(facesContext) +
                    "').updateNOW('" + escapeSingleQuote(nodeValue) + "');";
            encodeDynamicScript(facesContext, autoCompleteEntry, call);
*/
        } else {
            if (matches.hasNext()) {
                StringBuffer sb = new StringBuffer("<div>");
                SelectItem item = null;
                while (matches.hasNext() && rowCounter++ < rows) {
                    item = (SelectItem) matches.next();
                    String itemLabel = item.getLabel();
                    if (itemLabel == null) {
                        /*itemLabel = converterGetAsString(
                                facesContext, autoCompleteEntry, item.getValue());*/ // TODO: add converter support
						itemLabel = item.getValue().toString();
                    }
                    sb.append("<div>").append(itemLabel)
                            .append("</div>");
                }
                sb.append("</div>");
                String call = "ice.ace.Autocompleters[\"" + autoCompleteEntry.getClientId(facesContext) + "\"]" +
                        ".updateNOW('" + escapeSingleQuote(sb.toString()) + "');";
                encodeDynamicScript(facesContext, autoCompleteEntry, call);
            }
        }
    }

    public void encodeDynamicScript(FacesContext facesContext, UIComponent uiComponent, String call) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
		
		writer.startElement("span", null);
		writer.writeAttribute("id", clientId + "dynamic_script", null);
		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);
		writer.writeText(call, null);
		writer.endElement("script");
		writer.endElement("span");
	}
		
	public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {

	}

    private static String escapeSingleQuote(String text) {
        if (null == text) {
            return "";
        }
        char[] chars = text.toCharArray();
        StringBuilder buffer = new StringBuilder(chars.length);
        for (int index = 0; index < chars.length; index++) {
            char ch = chars[index];
            if (ch == '\'') {
                buffer.append("&#39;");
            } else {
                buffer.append(ch);
            }
        }

        return buffer.toString();
    }
	
    // taken from com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer
	public static void encodeParentAndChildren(FacesContext facesContext, UIComponent parent) throws IOException {
        parent.encodeBegin(facesContext);
        if (parent.getRendersChildren()) {
            parent.encodeChildren(facesContext);
        } else {
            if (parent.getChildCount() > 0) {
                Iterator children = parent.getChildren().iterator();
                while (children.hasNext()) {
                    UIComponent nextChild = (UIComponent) children.next();
                    if (nextChild.isRendered()) {
                        encodeParentAndChildren(facesContext, nextChild);
                    }
                }
            }
        }
        parent.encodeEnd(facesContext);
    }
}
