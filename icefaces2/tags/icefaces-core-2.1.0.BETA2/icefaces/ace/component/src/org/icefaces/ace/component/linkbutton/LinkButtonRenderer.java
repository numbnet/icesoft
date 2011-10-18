/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
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


@MandatoryResourceComponent("org.icefaces.ace.component.linkbutton.LinkButton")
public class LinkButtonRenderer extends Renderer {

    List <UIParameter> uiParamChildren;

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.containsKey("ice.event.captured")) {
            LinkButton link  = (LinkButton) uiComponent;
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
            String clientId = link.getClientId();
            if (clientId.equals(source)) {
                try {
                    uiComponent.queueEvent(new ActionEvent(uiComponent));
                } catch (Exception e) {}
            }
        }
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
        String styleClass = "yui-button yui-link-button";
        boolean disabled = linkButton.isDisabled();
        if (disabled) {
            styleClass += " yui-button-disabled yui-link-button-disabled";
        }
        String myStyleClass = linkButton.getStyleClass();
        if ((myStyleClass != null) && (!"".equals(myStyleClass) )) {
            styleClass += " " + myStyleClass;
        } 
        writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);

        String style = linkButton.getStyle();
        if (style != null && style.trim().length() > 0) {
            writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);
        }

        // first child
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        styleClass = "first-child";
        if ((myStyleClass != null) && (!"".equals(myStyleClass) )) {
            styleClass += " " + myStyleClass;
        }
        writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);

        if (disabled) {
//            writer.write((String) linkButton.getValue());
            return;
        }

        // button element
        writer.startElement(HTML.ANCHOR_ELEM, uiComponent);

        // Uncomment this for the so - called inline model onclick handler 
        writer.writeAttribute(HTML.ONCLICK_ATTR,
                              "return ice.component.linkButton.clickHandler(event, '" + clientId + "' );",
                              null);
        String temp;
        if ((temp = linkButton.getHref()) != null) {
            if (uiParamChildren != null) {
                temp += "?" + Utils.asParameterString( uiParamChildren );
            }
            writer.writeAttribute(HTML.HREF_ATTR, temp, null );
        } else {
            // if there's no href, install a default key handler to catch the enter key
            writer.writeAttribute(HTML.ONKEYDOWN_ATTR,
                              "return ice.component.linkButton.keyDownHandler(event, '" + clientId + "' );",
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
        String jsProps = jsonBuilder.entry("disabled", disabled).endMap().toString();

        JSONBuilder jBuild = JSONBuilder.create().
                                beginMap().
                                entry("singleSubmit", linkButton.isSingleSubmit()).
                                entry("doAction", doAction).
                                entry("hashCode",  sb.toString().hashCode()).
                                entry("ariaEnabled", ariaEnabled);
        
        if (doAction && uiParamChildren != null) {
            jBuild.entry("postParameters",  Utils.asStringArray(uiParamChildren) );
        }

        String params = "'" + clientId + "'," +
                         jsProps
                        + "," + jBuild.endMap().toString();

        String finalScript = "ice.component.linkButton.updateProperties(" + params + ");";
        ScriptWriter.insertScript(facesContext, uiComponent, finalScript);
        writer.endElement(HTML.DIV_ELEM);
    }
}