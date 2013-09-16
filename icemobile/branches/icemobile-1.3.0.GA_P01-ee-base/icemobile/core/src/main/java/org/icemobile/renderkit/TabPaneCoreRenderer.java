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

import org.icemobile.component.IMobiComponent;
import org.icemobile.component.ITabPane;

import java.io.IOException;
import java.util.logging.Logger;

import static org.icemobile.util.HTML.*;

public class TabPaneCoreRenderer extends BaseCoreRenderer {
    
    private static final Logger logger =
            Logger.getLogger(TabPaneCoreRenderer.class.toString());
    public static String LI_TAG = "<li ";
    public static String LI_TAG_END = "</li>";
    public static String START_TAG = "<";
    public static String CLOSE_TAG = "/>";
    public static String SPAN_TAG = "<span ";
    public static String SPAN_END_TAG = "</span>" ;
    public static String RIGHT_ANCHOR_TAG = ">";
    public static String STRING_QUOTE = "\"";
    private String mActiveContentClass = ITabPane.TABSET_ACTIVE_CONTENT_CLASS;
    private String mPassiveContentClass = ITabPane.TABSET_HIDDEN_PAGECLASS;

    public StringBuilder encodeBegin(IMobiComponent component, IResponseWriter writer, boolean isJSP, String parentId)
            throws IOException {
        ITabPane tabPane = (ITabPane)component;
        StringBuilder sb = new StringBuilder(LI_TAG);
        int index = tabPane.getIndex();
        sb.append(ID_ATTR).append("=").append(STRING_QUOTE).append(parentId).append("tab_");
        sb.append(tabPane.getIndex()).append(STRING_QUOTE);
        sb.append(ONCLICK_ATTR).append("=").append(STRING_QUOTE).append(getScript(tabPane, parentId, index)).append(STRING_QUOTE);
        sb.append(RIGHT_ANCHOR_TAG);
        sb.append(tabPane.getTitle());
        sb.append(LI_TAG_END);
        return sb;
    }
    public void encodeBegin(IMobiComponent component, IResponseWriter writer, String parentId)
         throws IOException{
        ITabPane tabPane = (ITabPane)component;
        int index = tabPane.getIndex();
        writer.startElement(LI_ELEM, component);
        writer.writeAttribute(ID_ATTR, parentId+"tab_"+index);
        writer.writeAttribute(ONCLICK_ATTR, getScript(tabPane, parentId, index));
        writer.writeText(tabPane.getTitle());
        writer.endElement(LI_ELEM);
    }

    public StringBuilder getScript(ITabPane tabPane, String parentId, int index){
        StringBuilder script = new StringBuilder("ice.mobi.tabsetController.showContent('");
        script.append(parentId).append("', this, ").append("{");
        script.append("singleSubmit: true, tIndex: ").append(index);
        script.append(",client: ").append(tabPane.isClient());
        script.append("});") ;
        return script;
    }
    /*
       wrapper for tab pane content used by JSP
     */
    public void encodeContentWrapperBegin(IMobiComponent component, IResponseWriter writer, String selectedId,
                                   String clientId )
            throws IOException {
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(ID_ATTR, clientId+"_wrapper");
        String myclass = mPassiveContentClass;
        if (selectedId.equals(clientId)) {
              myclass = mActiveContentClass;
        }
        writer.writeAttribute(CLASS_ATTR, myclass);
        writer.startElement(DIV_ELEM);
        writer.writeAttribute(ID_ATTR, clientId);
    }

    public void encodeContentEnd(IMobiComponent component, IResponseWriter writer) throws IOException{
        writer.endElement(DIV_ELEM);
        writer.endElement(DIV_ELEM);
    }

}