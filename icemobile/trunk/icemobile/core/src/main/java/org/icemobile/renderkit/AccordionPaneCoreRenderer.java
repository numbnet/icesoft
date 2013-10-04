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

import org.icemobile.component.IAccordion;
import org.icemobile.component.IContentPane;
import org.icemobile.util.ClientDescriptor;
import org.icemobile.util.Utils;
import org.icemobile.util.CSSUtils;


import java.io.IOException;
import java.util.logging.Logger;

import static org.icemobile.util.HTML.*;

public class AccordionPaneCoreRenderer extends BaseCoreRenderer {
    /*so far takes care of accordion, will add tabSet and contentPane as they are
      brought into core rendering strategy */
    private static final Logger logger =
            Logger.getLogger(ContentPaneCoreRenderer.class.toString());
    
    public void encodeBegin(IContentPane pane, IResponseWriter writer,
                            boolean isJsp, boolean selected)
            throws IOException{
        IAccordion accordion = pane.getAccordionParent();
        encodeAccordionHandle(accordion, pane, writer, isJsp, selected);
    }

    public void encodeEnd(IContentPane component, IResponseWriter writer, boolean isJsp)
             throws IOException {
        writer.endElement(DIV_ELEM);
        writer.endElement(DIV_ELEM);
        writer.endElement(DIV_ELEM);
    }
    public void encodeAccordionHandle(IAccordion accordion, IContentPane pane,
                  IResponseWriter writer, boolean isJsp, boolean selected )
         throws IOException{
        String accordionId = accordion.getClientId();
        String clientId = pane.getClientId();
        boolean client = pane.isClient();
        boolean autoheight = accordion.isAutoHeight();
        String myId = pane.getId();
        String handleClass = "handle " + CSSUtils.STYLECLASS_BAR_B;
        String pointerClass = "pointer";
        writer.startElement(DIV_ELEM, pane);
        writer.writeAttribute(ID_ATTR, clientId+"_sect");
           // apply default style class for panel-stack
        StringBuilder styleClass = new StringBuilder("closed");
        if (!isJsp && selected){
            styleClass = new StringBuilder("open") ;
        }
        String userDefinedClass = pane.getStyleClass();
        if (userDefinedClass != null && userDefinedClass.length() > 0){
             handleClass+= " " + userDefinedClass;
             pointerClass+=" " + userDefinedClass;
        }
        writer.writeAttribute(CLASS_ATTR, styleClass);
        writer.writeAttribute(STYLE_ATTR, pane.getStyle());
        writer.startElement(DIV_ELEM, pane);
        writer.writeAttribute(ID_ATTR, clientId+"_hndl");
        writer.writeAttribute(CLASS_ATTR, handleClass);
        StringBuilder args = new StringBuilder("ice.mobi.accordionController.toggleClient('");
        args.append(accordionId).append("', this, '").append(client).append("'");
        ClientDescriptor cd = accordion.getClient();
        if (Utils.isTransformerHack(cd)) {
            args.append(", true");
        }
        args.append(");");
        writer.writeAttribute("onclick", args.toString() );
        writer.startElement(SPAN_ELEM, pane);
        writer.writeAttribute("class", pointerClass);
        writer.endElement(SPAN_ELEM);
        String title = pane.getTitle();
        writer.writeText(title);
        writer.endElement(DIV_ELEM);
        writer.startElement(DIV_ELEM, pane);
        writer.writeAttribute(ID_ATTR, clientId+"wrp");
        String htString = accordion.getHeight();
    //    boolean scrollablePaneContent = accordion.isScrollablePaneContent();
        boolean scrollablePaneContent = false;  // leave false until Jira is targeted EE or 1.4 Beta
        StringBuilder style = new StringBuilder(256);
        if (!autoheight && (null != htString) && !htString.equals("") && scrollablePaneContent) {
           style.append( "height: ").append(htString).append("; overflow-y: auto;");
           writer.writeAttribute(STYLE_ATTR, style.toString()) ;
        }
        writer.startElement(DIV_ELEM, pane);
        writer.writeAttribute(ID_ATTR, clientId);
    }
}
