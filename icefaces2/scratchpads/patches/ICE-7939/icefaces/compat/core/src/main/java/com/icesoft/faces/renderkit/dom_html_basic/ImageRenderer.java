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

package com.icesoft.faces.renderkit.dom_html_basic;

import com.icesoft.faces.component.AttributeConstants;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.util.Debug;
import com.icesoft.faces.util.CoreUtils;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class ImageRenderer extends DomBasicRenderer {

    private static final String[] passThruAttributes = AttributeConstants.getAttributes(AttributeConstants.H_GRAPHICIMAGE);
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {

        validateParameters(facesContext, uiComponent, UIGraphic.class);
        UIGraphic uiGraphic = (UIGraphic) uiComponent;

        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);
        if (!domContext.isInitialized()) {
            Element root = domContext.createElement("img");
            domContext.setRootNode(root);
        }
        Element root = (Element) domContext.getRootNode();

        setRootElementId(facesContext, root, uiGraphic);

        String srcAttribute = processSrcAttribute(facesContext, uiGraphic);
        root.setAttribute("src", srcAttribute);

        String styleClass = String.valueOf(uiComponent.getAttributes().get("styleClass"));
        if (styleClass != null) {
            root.setAttribute("class", styleClass);
        }

        ResponseWriter responseWriter = facesContext.getResponseWriter();
        Debug.assertTrue(responseWriter != null, "ResponseWriter is null");
        PassThruAttributeRenderer
                .renderHtmlAttributes(facesContext, uiComponent, passThruAttributes);

        Object ismap = uiComponent.getAttributes().get("ismap");
        if (ismap instanceof Boolean && ((Boolean)ismap).booleanValue()) {
            root.setAttribute("ismap", "ismap");
        }
        
        String altAttribute = (String) uiComponent.getAttributes().get("alt");
        if (altAttribute == null) {
            altAttribute = "";
        }
        root.setAttribute("alt", altAttribute);
        
        domContext.stepOver();
    }

    public void encodeChildren(FacesContext facesContext,
                               UIComponent uiComponent) {
        validateParameters(facesContext, uiComponent, UIGraphic.class);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, UIGraphic.class);
    }

    protected String processSrcAttribute(FacesContext facesContext, UIGraphic
            uiGraphic) {
          String value = (String) uiGraphic.getValue();
          // support url as an alias for value
          if (value == null) {
              value = uiGraphic.getUrl();
          }
          if (value != null) {
              value = CoreUtils.resolveResourceURL(facesContext, value);
              return value;
          } else {
              return "";
          }
    }
}

