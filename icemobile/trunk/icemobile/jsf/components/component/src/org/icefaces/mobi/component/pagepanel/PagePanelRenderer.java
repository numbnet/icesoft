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
package org.icefaces.mobi.component.pagepanel;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.JSFUtils;
import org.icefaces.mobi.utils.PassThruAttributeWriter;


public class PagePanelRenderer extends BaseLayoutRenderer {

    private static Logger logger = Logger.getLogger(PagePanelRenderer.class.getName());

    @Override
    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
        PagePanel pagePanel = (PagePanel) component;
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = component.getClientId(facesContext);
        // render a top level div and apply the style and style class pass through attributes
        writer.startElement(HTML.DIV_ELEM, pagePanel);
        PassThruAttributeWriter.renderNonBooleanAttributes(writer, pagePanel,
                pagePanel.getCommonAttributeNames());
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_pgPnl", HTML.ID_ATTR);

        // find out if header and/or footer facets are present as this will directly 
        // effect which style classes to apply
        UIComponent headerFacet = pagePanel.getFacet(PagePanel.HEADER_FACET);
        UIComponent bodyFacet = pagePanel.getFacet(PagePanel.BODY_FACET);
        UIComponent footerFacet = pagePanel.getFacet(PagePanel.FOOTER_FACET);
        if (headerFacet == null && bodyFacet == null && footerFacet == null) {
            logger.warning("PagePanel header, body and footer were not defined, " +
                    "no content will be rendered by this component.");
        }

        // write header if present
        if (headerFacet != null) {
            writer.startElement(HTML.DIV_ELEM, pagePanel);
            writer.writeAttribute(HTML.CLASS_ATTR, PagePanel.HEADER_CLASS, HTML.CLASS_ATTR);
            writer.writeAttribute(HTML.ID_ATTR, clientId + "_pgPnlHdr", HTML.ID_ATTR);
            writer.startElement(HTML.DIV_ELEM, component);
            writer.writeAttribute(HTML.CLASS_ATTR, PagePanel.CTR_CLASS, HTML.STYLE_CLASS_ATTR);
            JSFUtils.renderChild(facesContext, headerFacet);
            writer.endElement(HTML.DIV_ELEM);
            writer.endElement(HTML.DIV_ELEM);
        }

        /// write body with no-footer or no-header considerations
        if (bodyFacet != null) {
            // build out style class depending on header footer visibility
            StringBuilder bodyStyleClass = new StringBuilder(PagePanel.BODY_CLASS);
            if (headerFacet == null) {
                bodyStyleClass.append(" ").append(PagePanel.BODY_NO_HEADER_CLASS);
            }
            if (footerFacet == null) {
                bodyStyleClass.append(" ").append(PagePanel.BODY_NO_FOOTER_CLASS);
            }
            writer.startElement(HTML.DIV_ELEM, pagePanel);
            writer.writeAttribute(HTML.CLASS_ATTR, bodyStyleClass, HTML.CLASS_ATTR);
            writer.writeAttribute(HTML.ID_ATTR, clientId + "_pgPnlBdy", HTML.ID_ATTR);
            JSFUtils.renderChild(facesContext, bodyFacet);
            writer.endElement(HTML.DIV_ELEM);
        }

        // write footer f present
        if (footerFacet != null) {
            writer.startElement(HTML.DIV_ELEM, pagePanel);
            writer.writeAttribute(HTML.CLASS_ATTR, PagePanel.FOOTER_CLASS, HTML.CLASS_ATTR);
            writer.writeAttribute(HTML.ID_ATTR, clientId + "_pgPnlFtr", HTML.ID_ATTR);
            writer.startElement(HTML.DIV_ELEM, component);
            writer.writeAttribute(HTML.CLASS_ATTR, PagePanel.CTR_CLASS, HTML.STYLE_CLASS_ATTR);
            JSFUtils.renderChild(facesContext, footerFacet);
            writer.endElement(HTML.DIV_ELEM);
            writer.endElement(HTML.DIV_ELEM);
        }

        // close the top level div 
        writer.endElement(HTML.DIV_ELEM);
    }

    @Override
    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
        //Rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

}
