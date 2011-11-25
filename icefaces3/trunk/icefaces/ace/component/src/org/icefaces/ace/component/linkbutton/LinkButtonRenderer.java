/*
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
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
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.icefaces.ace.component.linkbutton;

import java.io.IOException;
import java.util.*;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;


import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.ace.util.ScriptWriter;
import org.icefaces.ace.util.Utils;

import org.icefaces.util.EnvUtils;
import org.icefaces.render.MandatoryResourceComponent;

import org.icefaces.ace.renderkit.CoreRenderer;

@MandatoryResourceComponent(tagName="linkButton", value="org.icefaces.ace.component.linkbutton.LinkButton")
public class LinkButtonRenderer extends CoreRenderer {

    List <UIParameter> uiParamChildren;
	
	private static String[] excludedAttributes = {"onclick", "onkeydown", "hreflang", "href", "target"};

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
		LinkButton link  = (LinkButton) uiComponent;
        if (requestParameterMap.containsKey("ice.event.captured")) {
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
            String clientId = link.getClientId();
            if (clientId.equals(source)) {
                try {
                    uiComponent.queueEvent(new ActionEvent(uiComponent));
                } catch (Exception e) {}
            }
        }
		
		decodeBehaviors(facesContext, link);
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {

        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);

        LinkButton linkButton = (LinkButton) uiComponent;

        // capture any children UIParameter (f:param) parameters.
        uiParamChildren = Utils.captureParameters( linkButton );

        writer.startElement(HTML.DIV_ELEM, uiComponent );
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
		writer.writeAttribute(HTML.CLASS_ATTR, "ice-linkbutton", null);

        //writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_span", null);
        String styleClass = "yui-button yui-link-button ui-button ui-widget";
        boolean disabled = linkButton.isDisabled();
        if (disabled) {
            styleClass += " yui-button-disabled yui-link-button-disabled";
        }
        writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);

        // first child
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        styleClass = "first-child";
        writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);

        if (disabled) {
//            writer.write((String) linkButton.getValue());
            return;
        }

        // button element
        writer.startElement(HTML.ANCHOR_ELEM, uiComponent);
        String myStyleClass = linkButton.getStyleClass();
        if ((myStyleClass != null) && (!"".equals(myStyleClass) )) {
            writer.writeAttribute(HTML.CLASS_ATTR, myStyleClass, null);
        } 

		renderPassThruAttributes(facesContext, linkButton, HTML.LINK_ATTRS, excludedAttributes);
		
		String userOnclick = (String) linkButton.getAttributes().get("onclick");
		userOnclick = userOnclick == null ? "" : userOnclick + ";";
        // Uncomment this for the so - called inline model onclick handler 
        writer.writeAttribute(HTML.ONCLICK_ATTR, userOnclick +
                              "return ice.ace.linkButton.clickHandler(event, '" + clientId + "' );",
                              null);
        String temp;
        if ((temp = linkButton.getHref()) != null) {
            if (uiParamChildren != null) {
                temp += "?" + Utils.asParameterString( uiParamChildren );
            }
            writer.writeAttribute(HTML.HREF_ATTR, temp, null );
        } else {
			String userOnkeydown = (String) linkButton.getAttributes().get("onkeydown");
			userOnkeydown = userOnkeydown == null ? "" : userOnkeydown + ";";
            // if there's no href, install a default key handler to catch the enter key
            writer.writeAttribute(HTML.ONKEYDOWN_ATTR, userOnkeydown +
                              "return ice.ace.linkButton.keyDownHandler(event, '" + clientId + "' );",
                              null);
        } 

        if ((temp  = linkButton.getHrefLang()) != null) {
            writer.writeAttribute(HTML.HREFLANG_ATTR, temp , null );
        }
        if ((temp = linkButton.getTarget()) != null) {
            writer.writeAttribute(HTML.TARGET_ATTR, temp, null );
        } 
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);

        LinkButton linkButton = (LinkButton) uiComponent;
        String value = (String) linkButton.getValue();
        // put the value here to minimize impact in rendering
        writer.writeText(value, null);
        boolean disabled = linkButton.isDisabled();
        if (!disabled) {
            writer.endElement(HTML.ANCHOR_ELEM);
        }
        writer.endElement(HTML.SPAN_ELEM);
        writer.endElement(HTML.SPAN_ELEM);

        // With Action or ActionListener attributes, don't act as a normal link
        ActionListener[] al = linkButton.getActionListeners();
        boolean doAction = (al.length > 0 || (linkButton.getActionExpression() != null));

        StringBuilder sb = new StringBuilder();
        sb.append( value ).
                append(linkButton.getHref()).
                append(linkButton.getHrefLang()).
                append(linkButton.getStyleClass()).
                append(linkButton.getStyle()).
                append(linkButton.getTarget());

        boolean ariaEnabled = EnvUtils.isAriaEnabled(facesContext);
        Integer tabindex = linkButton.getTabindex();
        if (ariaEnabled && tabindex == null) tabindex = 0;

        JSONBuilder jsonBuilder = JSONBuilder.create().beginMap();
        jsonBuilder.entry("type", "link");
        if (tabindex != null) {
            jsonBuilder.entry("tabindex", tabindex);
        }
        jsonBuilder.entry("label", (String) linkButton.getValue());
        jsonBuilder.entry("disabled", disabled);
		encodeClientBehaviors(facesContext, linkButton, jsonBuilder);
		String jsProps = jsonBuilder.endMap().toString();

        JSONBuilder jBuild = JSONBuilder.create().
                                beginMap().
                                entry("doAction", doAction).
                                entry("hashCode",  sb.toString().hashCode()).
                                entry("ariaEnabled", ariaEnabled);
        
        if (doAction && uiParamChildren != null) {
            jBuild.entry("postParameters",  Utils.asStringArray(uiParamChildren) );
        }

        String params = "'" + clientId + "'," +
                         jsProps
                        + "," + jBuild.endMap().toString();

        String finalScript = "ice.ace.linkButton.updateProperties(" + params + ");";
        ScriptWriter.insertScript(facesContext, uiComponent, finalScript);
        writer.endElement(HTML.DIV_ELEM);
    }
}