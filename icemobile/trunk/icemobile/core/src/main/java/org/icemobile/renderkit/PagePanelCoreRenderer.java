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

package org.icemobile.renderkit;

import java.io.IOException;
import java.util.logging.Logger;

import org.icemobile.component.IPagePanel;
import static org.icemobile.util.HTML.*;

public class PagePanelCoreRenderer {
    
    private static Logger logger = Logger.getLogger(PagePanelCoreRenderer.class.getName());
    
    // base styles for the three page sections
    public static final String HEADER_CLASS = "mobi-pagePanel-header ui-header ui-header-fixed";
    public static final String FOOTER_CLASS = "mobi-pagePanel-footer ui-footer ui-footer-fixed";
    public static final String BODY_CLASS = "mobi-pagePanel-body";
    public static final String CTR_CLASS = "mobi-pagePanel-ctr";

    // style classes to remove header footer margins
    public static final String BODY_NO_HEADER_CLASS = "mobi-pagePanel-body-noheader";
    public static final String BODY_NO_FOOTER_CLASS = "mobi-pagePanel-body-nofooter";
   
    public void encode(IPagePanel component, IResponseWriter writer) throws IOException{
        
        String clientId = component.getClientId();
        
        Object header = component.getHeader();
        Object body = component.getBody();
        Object footer = component.getFooter();
        
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(STYLE_ATTR, component.getStyle());
        writer.writeAttribute(CLASS_ATTR, component.getStyleClass());
        if( clientId != null ){
            writer.writeAttribute(ID_ATTR, clientId + "_pgPnl");
        }

        // find out if header and/or footer facets are present as this will directly 
        // effect which style classes to apply
        if (header == null && footer == null && body == null) {
            logger.warning("PagePanel header, body and footer were not defined, " +
                    "no content will be rendered by this component.");
        }

        // write header if present
        if (header != null) {
            writer.startElement(DIV_ELEM, component);
            writer.writeAttribute(CLASS_ATTR, HEADER_CLASS);
            if( clientId != null ){
                writer.writeAttribute(ID_ATTR, clientId + "_pgPnlHdr");
            }
            writer.startElement(DIV_ELEM, component);
            writer.writeAttribute(CLASS_ATTR, CTR_CLASS);
            //writer.renderChild(header);
            writer.endElement(DIV_ELEM);
            writer.endElement(DIV_ELEM);
        }

        /// write body with no-footer or no-header considerations
        if (body != null) {
            // build out style class depending on header footer visibility
            StringBuilder bodyStyleClass = new StringBuilder(BODY_CLASS);
            if (header == null) {
                bodyStyleClass.append(" ").append(BODY_NO_HEADER_CLASS);
            }
            if (footer == null) {
                bodyStyleClass.append(" ").append(BODY_NO_FOOTER_CLASS);
            }
            writer.startElement(DIV_ELEM, component);
            writer.writeAttribute(CLASS_ATTR, bodyStyleClass);
            if( clientId != null ){
                writer.writeAttribute(ID_ATTR, clientId + "_pgPnlBdy");
            }
            //writer.renderChild(body);
            writer.endElement(DIV_ELEM);
        }

        // write footer present
        if (footer != null) {
            writer.startElement(DIV_ELEM, component);
            writer.writeAttribute(CLASS_ATTR, FOOTER_CLASS);
            if( clientId != null ){
                writer.writeAttribute(ID_ATTR, clientId + "_pgPnlFtr");
            }
            writer.startElement(DIV_ELEM, component);
            writer.writeAttribute(CLASS_ATTR, CTR_CLASS);
            //writer.renderChild(footer);
            writer.endElement(DIV_ELEM);
            writer.endElement(DIV_ELEM);
        }

        // close the top level div 
        writer.endElement(DIV_ELEM);
    }

}
