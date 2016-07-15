/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

import org.icemobile.component.IFragment;
import org.icemobile.component.ISplitPane;

import java.util.logging.Level;
import java.util.logging.Logger;
import static org.icemobile.util.HTML.*;

public class SplitPaneCoreRenderer extends BaseCoreRenderer {
    public static final String SPLITPANE_BASE_CSS = "mobi-splitpane" ;
    public static final String SPLITPANE_NONSCROLL_CSS = "mobi-splitpane-nonScrollable";
    public static final String SPLITPANE_SCROLLABLE_CSS = "mobi-splitpane-scrollable";
    public static final String SPLITPANE_DIVIDER_CSS = "mobi-splitpane-divider";

    private static final Logger logger =
            Logger.getLogger(SplitPaneCoreRenderer.class.toString());
    private static final int DEFAULT_COLUMN_WIDTH = 25;
    private String leftwidth;
    private String rightwidth;
    private StringBuilder leftPaneClass = new StringBuilder(SPLITPANE_SCROLLABLE_CSS); //default
    private StringBuilder rightPaneClass = new StringBuilder(SPLITPANE_SCROLLABLE_CSS); //default
    private StringBuilder spltClass = new StringBuilder(SPLITPANE_DIVIDER_CSS);

    public void encodeBegin(ISplitPane component, IResponseWriter writer)
            throws IOException {;
        if (!component.isScrollable()) {
            leftPaneClass = rightPaneClass = new StringBuilder(SPLITPANE_NONSCROLL_CSS) ;
        }
        leftPaneClass.append(" ").append("left");
        rightPaneClass.append(" ").append("right");
        int leftWidth = component.getColumnDivider();
        if (leftWidth < 1 || leftWidth>99){
            leftWidth = DEFAULT_COLUMN_WIDTH;
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(" input of ColumnDivider is invalid, setting it to default value");
            }
        }
        int rightWidth = 100 - leftWidth;
        leftwidth = (String.valueOf(leftWidth)+ "%");
        rightwidth = (String.valueOf(rightWidth) + "%");
        String userClass = component.getStyleClass();
        if (userClass!=null){
            leftPaneClass.append(" ").append(userClass) ;
            this.spltClass.append(" ").append(userClass).toString();
        }
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(ID_ATTR, component.getClientId());
        writeStandardLayoutAttributes(writer, component, SPLITPANE_BASE_CSS );
    }

    /**
     *  used by JSP tag for rendering fragment
     * @param component
     * @param writer
     * @param style
     * @throws IOException
     */
    public void encodePane(IFragment component, IResponseWriter writer, String style)
        throws IOException {
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(ID_ATTR, component.getClientId());
        writer.writeAttribute(CLASS_ATTR, SPLITPANE_SCROLLABLE_CSS);
        writer.writeAttribute(STYLE_ATTR, style);
    }
    public void encodePaneEnd(IResponseWriter writer)
        throws IOException{
        writer.endElement(DIV_ELEM);
    }

    /**
     *   used by jsf renderer  for facets
     * @param component
     * @param writer
     * @param side
     * @throws IOException
     */
    public void encodePane(ISplitPane component, IResponseWriter writer, String side)
        throws IOException {
        writer.startElement(DIV_ELEM, component);
        String width = this.getLeftwidth();
        if (side.equals("right")){
           width = this.getRightwidth();
        }
        writer.writeAttribute(STYLE_ATTR, width);
        if( "left".equals(side)){
            writer.writeAttribute(CLASS_ATTR, leftPaneClass);
        }
        else{
            writer.writeAttribute(CLASS_ATTR, rightPaneClass);
        }
        writer.writeAttribute(ID_ATTR, component.getClientId()+"_"+side);
    }

    public void encodeEnd(ISplitPane pane, IResponseWriter writer)
            throws IOException{
        writer.startElement(SPAN_ELEM, pane);
        writer.writeAttribute(CLASS_ATTR, "mobi-hidden");
        writer.startElement(SCRIPT_ELEM, pane);
        writer.writeAttribute("type", "text/javascript");
        StringBuilder sb = new StringBuilder("setTimeout(function(){ice.mobi.splitpane.initClient('").append(pane.getClientId()).append("'");
        sb.append(",{ scrollable: '").append(pane.isScrollable()).append("'");
        int width = pane.getColumnDivider();
        if (width < 1 || width > 99){
            width = DEFAULT_COLUMN_WIDTH;
        }
        sb.append(",width: '").append(width).append("'");
        sb.append("})},10);");
        writer.writeText(sb.toString());
        writer.endElement(SCRIPT_ELEM);
        writer.endElement(SPAN_ELEM);
        writer.endElement(DIV_ELEM);
    }

    public String getLeftwidth() {
        if (this.leftwidth != null){
            return "width:" +leftwidth+";";}
        else {
            return "width: 30%;"; //default value
        }
    }

    private String getRightwidth() {
        if (this.rightwidth !=null){
            return "width:" + rightwidth+";";
        } else {
            return "width: 70%;";
        }
    }

}
