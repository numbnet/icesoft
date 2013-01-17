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

package org.icefaces.ace.component.autocompleteentry;

import org.icefaces.ace.renderkit.InputRenderer;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.el.ELContext;
import javax.el.ValueExpression;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.io.IOException;

@MandatoryResourceComponent(tagName="autoCompleteEntry", value="org.icefaces.ace.component.autocompleteentry.AutoCompleteEntry")
public class AutoCompleteEntryRenderer extends InputRenderer {

    private static final String AUTOCOMPLETE_DIV = "_div";

    public boolean getRendersChildren() {
        return true;
    }
	
	public void decode(FacesContext facesContext, UIComponent uiComponent) {
		AutoCompleteEntry autoCompleteEntry = (AutoCompleteEntry) uiComponent;
		autoCompleteEntry.setPopulateList(false);
		autoCompleteEntry.setItemList(null);
        Map requestMap = facesContext.getExternalContext().getRequestParameterMap();
        String clientId = autoCompleteEntry.getClientId(facesContext);
        String value = (String) requestMap.get(clientId + "_input");
		String oldValue = (String) autoCompleteEntry.getValue();
		
        if (value != null) {
			if (autoCompleteEntry.isCaseSensitive()) {
				if (!value.equals(oldValue)) {
					autoCompleteEntry.setPopulateList(true);
				}
			} else {
				if (!value.equalsIgnoreCase(oldValue)) {
					autoCompleteEntry.setPopulateList(true);
				}			
			}
			if ("".equals(value) && oldValue == null) {
				autoCompleteEntry.setPopulateList(false);
			}
			autoCompleteEntry.setSubmittedValue(value);
        }
		
        KeyEvent keyEvent = new KeyEvent(autoCompleteEntry, requestMap);
		boolean isEventSource = false;
		Object sourceId = requestMap.get("ice.event.captured");
        if (sourceId != null && sourceId.toString().equals(clientId + "_input")) {
			isEventSource = true;
        }
        if (isEventSource) {
			if (isHardSubmit(facesContext, autoCompleteEntry)) {
					autoCompleteEntry.setPopulateList(false);
					autoCompleteEntry.queueEvent(new ActionEvent(autoCompleteEntry));
			} else if (keyEvent.getKeyCode() == KeyEvent.UP_ARROW_KEY || keyEvent.getKeyCode() == KeyEvent.DOWN_ARROW_KEY) {
				autoCompleteEntry.setPopulateList(true);
			}
        } else {
			autoCompleteEntry.setPopulateList(false);
		}
		if (keyEvent.getKeyCode() == KeyEvent.TAB) {
			autoCompleteEntry.setPopulateList(false);
		}
		
		decodeBehaviors(facesContext, autoCompleteEntry);
	}
	
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {

		ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        AutoCompleteEntry autoCompleteEntry = (AutoCompleteEntry) uiComponent;
        boolean ariaEnabled = EnvUtils.isAriaEnabled(facesContext);
		
		// root
        writer.startElement("div", null);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("class", autoCompleteEntry.getStyleClass(), null);

        Map paramMap = facesContext.getExternalContext().getRequestParameterMap();
        Map<String, Object> labelAttributes = getLabelAttributes(uiComponent);

        String inFieldLabel = (String) labelAttributes.get("inFieldLabel"), inFieldLabelStyleClass = "";
        String iceFocus = (String) paramMap.get("ice.focus");
        String value = (String) autoCompleteEntry.getValue();
        boolean labelIsInField = false;
        if (isValueBlank(value)) value = null;
		String inputClientId = clientId + "_input";
        if (value == null && !isValueBlank(inFieldLabel) && !inputClientId.equals(iceFocus)) {
            value = inFieldLabel;
            inFieldLabelStyleClass = " " + IN_FIELD_LABEL_STYLE_CLASS;
            labelIsInField = true;
        }

        writeLabelAndIndicatorBefore(labelAttributes);
        // text field
		writer.startElement("input", null);
        writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputClientId, null);
        if (ariaEnabled) {
            writer.writeAttribute("role", "textbox", null);
        }
        String mousedownScript = (String) uiComponent.getAttributes().get("onmousedown");
		mousedownScript = mousedownScript == null ? "" : mousedownScript;
		writer.writeAttribute("onmousedown", mousedownScript + "this.focus();", null);
		int width = autoCompleteEntry.getWidth();
		writer.writeAttribute("style", "width: " + width + "px;" + autoCompleteEntry.getStyle(), null);
        writer.writeAttribute("class", "ui-inputfield ui-widget ui-state-default ui-corner-all" + getStateStyleClasses(autoCompleteEntry) + inFieldLabelStyleClass, null);
		writer.writeAttribute("autocomplete", "off", null);
        String onfocusCombinedValue = "setFocus(this.id);";
        Object onfocusAppValue = uiComponent.getAttributes().get("onfocus");
        if (onfocusAppValue != null) {
            onfocusCombinedValue += onfocusAppValue.toString();
		}
        writer.writeAttribute("onfocus", onfocusCombinedValue, null);
        String onblurCombinedValue = "";
        Object onblurAppValue = uiComponent.getAttributes().get("onblur");
        if (onblurAppValue != null) {
            onblurCombinedValue += onblurAppValue.toString();
		}
        writer.writeAttribute("onblur", onblurCombinedValue, null);
        Object onchangeAppValue = uiComponent.getAttributes().get("onchange");
        if (onchangeAppValue != null) {
            writer.writeAttribute("onchange", onchangeAppValue.toString(), null);
		}
        if (value != null) {
			// if field is required and is now empty, avoid displaying the previous valid value
			Object submittedValue = autoCompleteEntry.getSubmittedValue();
			if (submittedValue != null) {
				if ("".equals((String) submittedValue) && !"".equals(value) && autoCompleteEntry.isRequired()) value = "";
			}
			
			//writer.writeAttribute("value", value, null);
		} else {
			//writer.writeAttribute("value", "", null);
			value = "";
		}

        if (ariaEnabled) {
            final AutoCompleteEntry compoent = (AutoCompleteEntry) uiComponent;
            Map<String, Object> ariaAttributes = new HashMap<String, Object>() {{
                put("autocomplete", "list");
                put("readonly", compoent.isReadonly());
                put("required", compoent.isRequired());
                put("disabled", compoent.isDisabled());
                put("invalid", !compoent.isValid());
            }};
            writeAriaAttributes(ariaAttributes, labelAttributes);
        }

        writer.endElement("input");
        writeLabelAndIndicatorAfter(labelAttributes);

		// div
		writer.startElement("div", null);
		String divId = clientId + AUTOCOMPLETE_DIV;
		writer.writeAttribute("id", clientId + AUTOCOMPLETE_DIV, null);
		writer.writeAttribute("class", "ui-widget ui-widget-content ui-corner-all", null);
		writer.writeAttribute("style", "display:none;z-index:500;", null);
		writer.endElement("div");

		// script
		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);
		String direction = autoCompleteEntry.getDirection();
		direction = direction != null ? ("up".equalsIgnoreCase(direction) || "down".equalsIgnoreCase(direction) ? direction : "auto" ) : "auto";
        boolean isEventSource = false;
		Object sourceId = paramMap.get("ice.event.captured");
        if (sourceId != null) {
            if (sourceId.toString().equals(inputClientId)) {
                isEventSource = true;
            }
        }
		boolean isBlurEvent = false;
		KeyEvent keyEvent = new KeyEvent(autoCompleteEntry, paramMap);
		Object event = paramMap.get("javax.faces.behavior.event");
		if (keyEvent.getKeyCode() == KeyEvent.TAB || (event != null && event.toString().equals("blur"))) {
			isBlurEvent = true;
        }
		boolean focus = isEventSource && !isBlurEvent;
		if (!autoCompleteEntry.isDisabled() && !autoCompleteEntry.isReadonly()) {
			JSONBuilder jb = JSONBuilder.create();
			jb.beginFunction("ice.ace.Autocompleter")
				.item(clientId)
				.item(divId)
				.item("ui-widget-content")
				.item("ui-state-active")
				.item(autoCompleteEntry.getDelay())
				.item(autoCompleteEntry.getMinChars())
				.item(autoCompleteEntry.getHeight())
				.item(direction)
				.beginMap()
				.entry("p", ""); // dummy property
				encodeClientBehaviors(facesContext, autoCompleteEntry, jb);
			jb.endMap();
            jb.beginMap()
                .entryNonNullValue("inFieldLabel", inFieldLabel)
                .entry("inFieldLabelStyleClass", IN_FIELD_LABEL_STYLE_CLASS)
                .entry("labelIsInField", labelIsInField);
            jb.endMap();
			if (autoCompleteEntry.isClientSide()) {
				int rows = autoCompleteEntry.getRows();
				if (rows == 0) rows = Integer.MAX_VALUE;
				FilterMatchMode filterMatchMode = getFilterMatchMode(autoCompleteEntry);
				jb.beginMap()
					.entry("rows", rows)
					.entry("filterMatchMode", filterMatchMode.toString())
					.entry("caseSensitive", autoCompleteEntry.isCaseSensitive())
				.endMap();
			} else {
				jb.item("null", false);
			}
			jb.endFunction();
			writer.writeText("new " + jb.toString(), null);
		}
		
		writer.endElement("script");
		
		// field update script
		writer.startElement("span", null);
		writer.writeAttribute("id", clientId + "_fieldupdate", null);
		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);
		writer.writeText("(function() {", null);
		writer.writeText("var instance = ice.ace.Autocompleters[\"" + clientId + "\"];", null);
		writer.writeText("instance.updateField('" + escapeBackslashes(value) + "', " + focus + ");", null);
		writer.writeText("})();", null);
		writer.endElement("script");
		writer.endElement("span");
		writer.endElement("div");
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
        AutoCompleteEntry autoCompleteEntry = (AutoCompleteEntry) uiComponent;
		String clientId = autoCompleteEntry.getClientId(facesContext);
		if (autoCompleteEntry.isClientSide()) {
			populateClientSideList(facesContext, autoCompleteEntry);
		} else if (autoCompleteEntry.getValue() != null && autoCompleteEntry.isPopulateList()) {
			populateList(facesContext, autoCompleteEntry);
        } else {
            writer.startElement("div", null);
			writer.writeAttribute("id", clientId + "_update", null);
			String call = "ice.ace.Autocompleters[\"" + clientId + "\"].updateNOW('');";
			encodeDynamicScript(facesContext, autoCompleteEntry, call);
			writer.endElement("div");
		}
    }

    public void populateList(FacesContext facesContext, AutoCompleteEntry autoCompleteEntry) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		String clientId = autoCompleteEntry.getClientId(facesContext);
        autoCompleteEntry.populateItemList();
        Iterator matches = autoCompleteEntry.getItemListIterator();
		String filter = ((String) autoCompleteEntry.getValue());
		FilterMatchMode filterMatchMode = getFilterMatchMode(autoCompleteEntry);
		String mainValue = (String) autoCompleteEntry.getValue();
		String timestamp = getTimestamp(facesContext, autoCompleteEntry);
        int rows = autoCompleteEntry.getRows();
        if (rows == 0) rows = Integer.MAX_VALUE;
        int rowCounter = 0;
        writer.startElement("div", null);
		writer.writeAttribute("id", clientId + "_update", null);
        if (autoCompleteEntry.getSelectFacet() != null) {

            UIComponent facet = autoCompleteEntry.getSelectFacet();
			ValueExpression filterBy = autoCompleteEntry.getValueExpression("filterBy");
			ELContext elContext = facesContext.getELContext();
			String listVar = autoCompleteEntry.getListVar();

            writer.startElement("div", null);
			writer.writeAttribute("style", "display: none;", null);
			writer.startElement("div", null);
            Map requestMap = facesContext.getExternalContext().getRequestMap();
            //set index to 0, so child components can get client id from autoComplete component
            autoCompleteEntry.setIndex(0);
            while (matches.hasNext() && rowCounter < rows) {

				requestMap.put(listVar, matches.next());
				String value = (String) filterBy.getValue(elContext);
			
				if (satisfiesFilter(value, filter, filterMatchMode, autoCompleteEntry)) {
					rowCounter++;
					writer.startElement("div", null);
					writer.writeAttribute("style", "border: 0;", null);
					
					// When HTML is display we still need a selected value. Hidding the value in a hidden span
					// accomplishes this.
					writer.startElement("span", null); // span to display
					writer.writeAttribute("class", "informal", null);
					encodeParentAndChildren(facesContext, facet);
					writer.endElement("span");
					writer.startElement("span", null); // span to select
					writer.writeAttribute("style", "visibility:hidden;display:none;", null);
					String itemLabel;
					try {
						itemLabel = (String) getConvertedValue(facesContext, autoCompleteEntry, value, true);
					} catch (Exception e) {
						itemLabel = value;
					}
					writer.writeText(itemLabel, null);
					writer.endElement("span");
					autoCompleteEntry.resetId(facet);
					writer.endElement("div");
				}
				
				requestMap.remove(listVar);
            }
            autoCompleteEntry.setIndex(-1);

			writer.endElement("div");
            String call = "ice.ace.Autocompleters[\"" + clientId +
                    "\"].updateNOW(ice.ace.jq(ice.ace.escapeClientId('" + clientId + "_update')).get(0).firstChild.innerHTML);";
            encodeDynamicScript(facesContext, autoCompleteEntry, call + "// " + mainValue + " " + timestamp);
			writer.endElement("div");
        } else {
            if (matches.hasNext()) {
                StringBuffer sb = new StringBuffer("<div>");
                SelectItem item = null;
                while (matches.hasNext() && rowCounter < rows) {
                    item = (SelectItem) matches.next();
                    String itemLabel = item.getLabel();
                    if (itemLabel == null) {
						try {
							itemLabel = (String) getConvertedValue(facesContext, autoCompleteEntry, item.getValue(), true);
						} catch (Exception e) {
							itemLabel = item.getValue().toString();
						}
                    }
					if (satisfiesFilter(itemLabel, filter, filterMatchMode, autoCompleteEntry)) {
                    sb.append("<div style=\"border: 0;\">").append(itemLabel).append("</div>");
							rowCounter++;
					}
                }
                sb.append("</div>");
                String call = "ice.ace.Autocompleters[\"" + clientId + "\"]" +
                        ".updateNOW('" + escapeSingleQuote(sb.toString()) + "');";
                encodeDynamicScript(facesContext, autoCompleteEntry, call + "// " + mainValue + " " + timestamp);
            }
        }
		writer.endElement("div");
    }
	
    public void populateClientSideList(FacesContext facesContext, AutoCompleteEntry autoCompleteEntry) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		String clientId = autoCompleteEntry.getClientId(facesContext);
        autoCompleteEntry.populateItemList();
        Iterator matches = autoCompleteEntry.getItemListIterator();
        writer.startElement("div", null);
		writer.writeAttribute("id", clientId + "_update", null);
		writer.startElement("div", null);
		writer.writeAttribute("style", "display: none;", null);
        if (autoCompleteEntry.getSelectFacet() != null) {
			writer.writeAttribute("class", "facet", null);

            UIComponent facet = autoCompleteEntry.getSelectFacet();
			ValueExpression filterBy = autoCompleteEntry.getValueExpression("filterBy");
			ELContext elContext = facesContext.getELContext();
			String listVar = autoCompleteEntry.getListVar();

            Map requestMap = facesContext.getExternalContext().getRequestMap();
            //set index to 0, so child components can get client id from autoComplete component
            autoCompleteEntry.setIndex(0);
            while (matches.hasNext()) {

				requestMap.put(listVar, matches.next());
				String value = (String) filterBy.getValue(elContext);
			
				writer.startElement("div", null);
				writer.writeAttribute("style", "border: 0;", null);
				
				// When HTML is display we still need a selected value. Hidding the value in a hidden span
				// accomplishes this.
				writer.startElement("span", null); // span to display
				writer.writeAttribute("class", "informal", null);
				encodeParentAndChildren(facesContext, facet);
				writer.endElement("span");
				writer.startElement("span", null); // span to select
				writer.writeAttribute("class", "label", null);
				writer.writeAttribute("style", "visibility:hidden;display:none;", null);
				String itemLabel;
				try {
					itemLabel = (String) getConvertedValue(facesContext, autoCompleteEntry, value, true);
				} catch (Exception e) {
					itemLabel = value;
				}
				writer.writeText(itemLabel, null);
				writer.endElement("span");
				autoCompleteEntry.resetId(facet);
				writer.endElement("div");
				
				requestMap.remove(listVar);
            }
            autoCompleteEntry.setIndex(-1);
        } else {
            if (matches.hasNext()) {
                StringBuffer sb = new StringBuffer();
                SelectItem item = null;
                while (matches.hasNext()) {
                    item = (SelectItem) matches.next();
                    String itemLabel = item.getLabel();
                    if (itemLabel == null) {
						try {
							itemLabel = (String) getConvertedValue(facesContext, autoCompleteEntry, item.getValue(), true);
						} catch (Exception e) {
							itemLabel = item.getValue().toString();
						}
                    }
                    sb.append("<div style=\"border: 0;\">").append(itemLabel).append("</div>");
                }
				writer.write(escapeSingleQuote(sb.toString()));
            }
        }
		writer.endElement("div");
		writer.endElement("div");
    }

    public void encodeDynamicScript(FacesContext facesContext, UIComponent uiComponent, String call) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
		
		writer.startElement("span", null);
		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);
		writer.writeText(call, null);
		writer.endElement("script");
		writer.endElement("span");
	}
		
	public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {

	}
	
	private boolean isHardSubmit(FacesContext facesContext, UIComponent component) {
		Map requestMap = facesContext.getExternalContext().getRequestParameterMap();
		String clientId = component.getClientId(facesContext);
		KeyEvent keyEvent = new KeyEvent(component, requestMap);
		
		return (keyEvent.getKeyCode() == KeyEvent.CARRIAGE_RETURN ||
			"true".equals(requestMap.get("ice.event.left")) ||
			"onclick".equals(requestMap.get("ice.event.type")) ||
			requestMap.containsKey(clientId + "_hardSubmit"));
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
	
	private static String escapeBackslashes(String str) {
		return str.replace("\\", "\\\\");
	}
	
	private String getTimestamp(FacesContext facesContext, AutoCompleteEntry autoCompleteEntry) {
		Map requestMap = facesContext.getExternalContext().getRequestParameterMap();
		Object sourceId = requestMap.get("ice.event.captured");
		String clientId = autoCompleteEntry.getClientId(facesContext);
		KeyEvent keyEvent = new KeyEvent(autoCompleteEntry, requestMap);
		String timestamp = "";
		// only include a timestamp if the user pressed the up or down arrow keys to introduce a difference from previous dom
		// so that the domdiff sends the call to update the list of options
		if (keyEvent.getKeyCode() == KeyEvent.UP_ARROW_KEY || keyEvent.getKeyCode() == KeyEvent.DOWN_ARROW_KEY) {
			timestamp = "" + System.currentTimeMillis();
		}
		return timestamp;
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
	
	private FilterMatchMode getFilterMatchMode(AutoCompleteEntry autoCompleteEntry) {
		String filterMatchMode = autoCompleteEntry.getFilterMatchMode();
		if ("contains".equalsIgnoreCase(filterMatchMode)) return FilterMatchMode.contains;
		if ("exact".equalsIgnoreCase(filterMatchMode)) return FilterMatchMode.exact;
		if ("endsWith".equalsIgnoreCase(filterMatchMode)) return FilterMatchMode.endsWith;
		if ("none".equalsIgnoreCase(filterMatchMode)) return FilterMatchMode.none;
		return FilterMatchMode.startsWith;
	}
	
	private enum FilterMatchMode {
		contains,
		exact,
		startsWith,
		endsWith,
		none
	}
	
	private boolean satisfiesFilter(String string, String filter, FilterMatchMode filterMatchMode, AutoCompleteEntry autoCompleteEntry) {
		
		if (string != null) {
			if (!autoCompleteEntry.isCaseSensitive()) {
				string = string.toLowerCase();
				filter = filter.toLowerCase();
			}
			switch (filterMatchMode) {
				case contains:
					if (string.indexOf(filter) >= 0) return true;
					break;
				case exact:
					if (string.equals(filter)) return true;
					break;
				case startsWith:
					if (string.startsWith(filter)) return true;
					break;
				case endsWith:
					if (string.endsWith(filter)) return true;
					break;
				default:
					return true;
			}
		}
		
		return false;
	}
	
	@Override
	public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
		return getConvertedValue(context, component, submittedValue, false);
	}
	
	private Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue, boolean force) throws ConverterException {
		AutoCompleteEntry autoCompleteEntry = (AutoCompleteEntry) component;
		String value = (String) submittedValue;
		Converter converter = autoCompleteEntry.getConverter();
		
		if (isHardSubmit(context, autoCompleteEntry) || force) {
			if(converter != null) {
				return converter.getAsObject(context, autoCompleteEntry, value);
			}
			else {
				ValueExpression ve = autoCompleteEntry.getValueExpression("value");

				if(ve != null) {
					Class<?> valueType = ve.getType(context.getELContext());
					Converter converterForType = context.getApplication().createConverter(valueType);

					if(converterForType != null) {
						return converterForType.getAsObject(context, autoCompleteEntry, value);
					}
				}
			}
		}
		
		return value;	
	}
}
