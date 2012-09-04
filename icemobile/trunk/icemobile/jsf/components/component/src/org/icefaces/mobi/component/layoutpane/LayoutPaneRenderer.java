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
package org.icefaces.mobi.component.layoutpane;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.Utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class LayoutPaneRenderer extends BaseLayoutRenderer {
    private static Logger logger = Logger.getLogger(LayoutPaneRenderer.class.getName());
    private static final String JS_NAME = "scrollable.js";
    private static final String JS_MIN_NAME = "scrollable-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.util";

    @Override
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)throws IOException {
         ResponseWriter writer = facesContext.getResponseWriter();
         String clientId = uiComponent.getClientId(facesContext);
         LayoutPane pane = (LayoutPane)uiComponent;
            /* write out root tag.  For current incarnation html5 semantic markup is ignored */

        UIComponent singleFacet = pane.getFacet(LayoutPane.SINGLE_FACET);
        UIComponent leftFacet = pane.getFacet(LayoutPane.LEFT_FACET);
        UIComponent rightFacet = pane.getFacet(LayoutPane.RIGHT_FACET);
        if (singleFacet == null && (leftFacet == null && rightFacet == null)) {
            logger.warning("This component requires either single facet OR " +
                    " left and right facets to be defined to render content.");
            return;
        }
        if (singleFacet != null && leftFacet !=null && rightFacet !=null){
            logger.warning("This component may ONLY have either one occurence of the singleFacet OR " +
                    "both the left and right facets together");
        }
        if (singleFacet !=null ){
            writer.startElement(HTML.DIV_ELEM, uiComponent);
            writer.writeAttribute(HTML.ID_ATTR, clientId+"_wrp", HTML.ID_ATTR);
            writer.startElement(HTML.DIV_ELEM, uiComponent);
            writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
            String baseClass = LayoutPane.LAYOUTPANE_CLASS;
            if (pane.isScrollable()){
                baseClass =  LayoutPane.LAYOUTPANE_SCROLL;
            }
            Utils.renderChild(facesContext, singleFacet);
            writer.endElement(HTML.DIV_ELEM);
        }
        if ((leftFacet!=null) && (rightFacet !=null)){
            writeJavascriptFile(facesContext, uiComponent, JS_NAME, JS_MIN_NAME, JS_LIBRARY);
            writer.startElement(HTML.DIV_ELEM, uiComponent);
            writer.writeAttribute(HTML.ID_ATTR, clientId+"_wrp", HTML.ID_ATTR);
            writer.startElement(HTML.DIV_ELEM, uiComponent);
            writer.writeAttribute(HTML.ID_ATTR, clientId+"_left", HTML.ID_ATTR);
            String baseClass = LayoutPane.LAYOUTPANE_LEFT;
            if (pane.isScrollable()){
                baseClass =  LayoutPane.LAYOUTPANE_LEFT_SCROLLABLE;
            }
            writer.writeAttribute("class", baseClass, null);
            Utils.renderChild(facesContext, leftFacet);
            writer.endElement(HTML.DIV_ELEM);
            writer.startElement(HTML.DIV_ELEM, uiComponent);
            writer.writeAttribute(HTML.ID_ATTR, clientId+"_right", HTML.ID_ATTR);
            writer.writeAttribute("class", baseClass, null);
            Utils.renderChild(facesContext, rightFacet);
            writer.endElement(HTML.DIV_ELEM);
            encodeScript(facesContext,  uiComponent);
            writer.endElement(HTML.DIV_ELEM);
        }
    }

    @Override
    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
        //Rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    private void encodeScript(FacesContext facesContext, UIComponent uiComponent) throws IOException{
        ResponseWriter writer = facesContext.getResponseWriter();
        LayoutPane pane = (LayoutPane) uiComponent;
        String clientId = pane.getClientId(facesContext);
        writer.startElement("span", uiComponent);
        writer.startElement("script", uiComponent);
        writer.writeAttribute("text", "text/javascript", null);
        StringBuilder sb = new StringBuilder("mobi.layout.initClient('").append(clientId).append("'");
        sb.append(",{ scrollable: '").append(pane.isScrollable()).append("'");
     //   sb.append(", resize: ").append(pane.isResizable());
        int width = pane.getWidth();
        sb.append(",width: '").append(width).append("'");
        sb.append("});");
        writer.write(sb.toString());
        writer.endElement("script");
        writer.endElement("span");
    }
}
