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
package org.icefaces.mobi.component.splitpane;


import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.JSFUtils;

public class SplitPaneRenderer extends BaseLayoutRenderer {
    private static Logger logger = Logger.getLogger(SplitPaneRenderer.class.getName());
    private static final String JS_NAME = "splitpane.js";
    private static final String JS_MIN_NAME = "splitpane-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.splitpane";

    @Override
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)throws IOException {
         ResponseWriter writer = facesContext.getResponseWriter();
         String clientId = uiComponent.getClientId(facesContext);
         SplitPane pane = (SplitPane)uiComponent;
            /* write out root tag.  For current incarnation html5 semantic markup is ignored */

        UIComponent leftFacet = pane.getFacet(SplitPane.LEFT_FACET);
        UIComponent rightFacet = pane.getFacet(SplitPane.RIGHT_FACET);
        if ( leftFacet ==null && rightFacet ==null){
            logger.warning("This component may ONLY have  " +
                    "both the left and right facets together");
        }
        StringBuilder baseClass = new StringBuilder(SplitPane.SPLITPANE_BASE);
        StringBuilder paneClass = new StringBuilder(SplitPane.SPLITPANE_SCROLLABLE) ;
        StringBuilder spltClass = new StringBuilder(SplitPane.SPLITPANE_DIVIDER) ;
        if (!pane.isScrollable()) {
            paneClass = new StringBuilder(SplitPane.SPLITPANE_NONSCROLL) ;
        }
        int leftWidth = pane.getColumnDivider();
        int rightWidth = 100- leftWidth;
        StringBuilder left = new StringBuilder(String.valueOf(leftWidth)).append("%;");
        StringBuilder right = new StringBuilder(String.valueOf(rightWidth)).append("%;");
        String userClass = pane.getStyleClass();
        if (userClass!=null){
            baseClass.append(" ").append(userClass) ;
            paneClass.append(" ").append(userClass);
            spltClass.append(" ").append(spltClass);
        }
        if ((leftFacet!=null) && (rightFacet !=null)){
            writeJavascriptFile(facesContext, uiComponent, JS_NAME, JS_MIN_NAME, JS_LIBRARY);
            writer.startElement(HTML.DIV_ELEM, uiComponent);
            if (pane.getStyle() !=null){
                writer.writeAttribute(HTML.STYLE_ATTR, pane.getStyle(), HTML.STYLE_ATTR);
            }
            writer.writeAttribute(HTML.CLASS_ATTR, baseClass, HTML.CLASS_ATTR);
            writer.writeAttribute(HTML.ID_ATTR, clientId+"_wrp", HTML.ID_ATTR);
            writer.startElement(HTML.DIV_ELEM, uiComponent);
            writer.writeAttribute(HTML.ID_ATTR, clientId+"_left", HTML.ID_ATTR);
            writer.writeAttribute("class", paneClass, null);
            writer.writeAttribute(HTML.STYLE_ATTR, left, HTML.STYLE_ATTR);
            JSFUtils.renderChild(facesContext, leftFacet);
            writer.endElement(HTML.DIV_ELEM);
            /* column Divider */
            writer.startElement(HTML.DIV_ELEM, uiComponent);
            writer.writeAttribute(HTML.ID_ATTR, clientId + "_splt", HTML.ID_ATTR);
            writer.writeAttribute(HTML.CLASS_ATTR, spltClass, HTML.CLASS_ATTR);
            //with resizable, will have a span styled as a button to close left panel
            writer.endElement(HTML.DIV_ELEM);
            /* right side */
            writer.startElement(HTML.DIV_ELEM, uiComponent);
            writer.writeAttribute(HTML.ID_ATTR, clientId+"_right", HTML.ID_ATTR);
            writer.writeAttribute("class", paneClass, null);
            writer.writeAttribute(HTML.STYLE_ATTR, right, HTML.STYLE_ATTR);
            JSFUtils.renderChild(facesContext, rightFacet);
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
        SplitPane pane = (SplitPane) uiComponent;
        String clientId = pane.getClientId(facesContext);
        writer.startElement("span", uiComponent);
        writer.startElement("script", uiComponent);
        writer.writeAttribute("text", "text/javascript", null);
        StringBuilder sb = new StringBuilder("mobi.splitpane.initClient('").append(clientId).append("'");
        sb.append(",{ scrollable: '").append(pane.isScrollable()).append("'");
     //   sb.append(", resize: ").append(pane.isResizable());
        int width = pane.getColumnDivider();
        sb.append(",width: '").append(width).append("'");
        sb.append("});");
        writer.write(sb.toString());
        writer.endElement("script");
        writer.endElement("span");
    }
}
