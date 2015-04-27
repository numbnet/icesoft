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
package org.icefaces.mobi.component.tabset;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.mobi.component.contentpane.ContentPane;
import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.JSFUtils;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.renderkit.TabSetCoreRenderer;
import org.icemobile.renderkit.IResponseWriter;
import org.icemobile.component.ITabSet;

public class TabSetRenderer extends BaseLayoutRenderer {
    private static final Logger logger = Logger.getLogger(TabSetRenderer.class.getName());
    private static final String JS_NAME = "tabset.js";
    private static final String JS_MIN_NAME = "tabset-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.tabset";

    @Override
    public void decode(FacesContext context, UIComponent component) {
        TabSet tabset = (TabSet) component;
        String clientId = tabset.getClientId(context);
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        // no ajax behavior defined yet
        String indexStr = params.get(clientId + "_hidden");
        // with some panes having client cacheType, the oldIndex on server
        // may not match that of the client, so decode the last
        if (null != indexStr) {
            try {
                 String submittedStr = indexStr;
                 int ind = indexStr.indexOf(",");
                 if (ind > -1){
                     String [] split = indexStr.split(",");
                     submittedStr = split[0];
                     tabset.setHashVal(String.valueOf(MobiJSFUtils.generateHashCode(indexStr)));
                     int index = Integer.parseInt(submittedStr);
                     TabSet.IdIndex oldIdIndex = tabset.resolveCurrentIdAndIndex();
                     int oldIndex = oldIdIndex.getIndex();
                     if (oldIndex != index) {
                        String oldId = tabset.getSelectedId();
                        String newId = component.getChildren().get(index).getId();
                        tabset.setSelectedId(newId);
                        component.queueEvent(new ValueChangeEvent(component, oldId, newId));
                       // tabset.setUpdatePropScriptTag(true);
                        //TO Do decode behaviors for mobi ajax support
                    }
                 } else {
                     tabset.setHashVal(null);
                 }
            } catch (NumberFormatException nfe) {
                if (logger.isLoggable(Level.FINER)) {
                    logger.finer("problem decoding tabIndex from client");
                }
            }
        }

    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        TabSet tabset = (TabSet) uiComponent;
        String clientId = tabset.getClientId();
        IResponseWriter writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        TabSetCoreRenderer renderer = new TabSetCoreRenderer();
        if (tabset.getSelectedId()==null && tabset.getDefaultId()!=null){
             tabset.setSelectedId(tabset.getDefaultId());
        }
        String orientation = tabset.getOrientation();
        boolean top = tabset.setIsTop(orientation);
        /* need to also see if pagePanel footer or header is present */
        tabset.setParentHeaderFooter();
        renderer.encodeBegin(tabset, writer, false, top);
        if (top ){
            encodeTabs(facesContext, uiComponent);
        }
        renderer.writeStartOfTabContents(tabset, writer, clientId);
    }

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        JSFUtils.renderChildren(facesContext, uiComponent);
    }

    public void encodeTabs(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        TabSet controller = (TabSet) uiComponent;
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = controller.getClientId(facesContext);
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_tabs", HTML.ID_ATTR);
        String baseTabsClass = ITabSet.TABSET_TABS_CLASS.toString();
        baseTabsClass += " mobi-tabset-tabs";
        writer.writeAttribute("class", baseTabsClass, "class");
    //    int tabsNum = uiComponent.getChildCount(); //MOBI-1116 just the rendered children!
        int tabsNum = controller.getRenderedChildCount();
        if (tabsNum <= 0) {
            if (logger.isLoggable(Level.FINER)) {
                logger.finer(" no contentPane children for this component. Please read DOCS");
            }
            writer.endElement(HTML.DIV_ELEM);
            return;
        }

        controller.resolveCurrentIdAndIndex();
        boolean autoWidth = controller.isAutoWidth();
        // calculate the percent width for th tabs
        int percent = 100/tabsNum;
        String percentWidth = "width:" + percent + "%;";
        String lastPercentWidth = null;
        if( percent * tabsNum != 100){
            lastPercentWidth = "width:" + (percent+(100-(percent*tabsNum)))+"%;";
        }
        else{
            lastPercentWidth = percentWidth;
        }

        writer.startElement(HTML.UL_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_dc", HTML.ID_ATTR);
        for (int i = 0; i < tabsNum; i++) {
            //check to see that children are of type contentPane
            UIComponent child = controller.getChildren().get(i);
            if (child instanceof ContentPane) {
                ContentPane cp = (ContentPane) child;
                if (cp.isRendered()){
                    boolean client = cp.isClient();
                    writer.startElement(HTML.LI_ELEM, uiComponent);
                    writer.writeAttribute(HTML.ID_ATTR, clientId + "tab_" + i, HTML.ID_ATTR);
                    StringBuilder sb = new StringBuilder("ice.mobi.tabsetController.showContent('").append(clientId);
                    sb.append("', this, ").append("{");
                    sb.append("singleSubmit: true, tIndex: ").append(i);
                    sb.append(",client: ").append(client);
                    sb.append("});");
                    writer.writeAttribute("onclick", sb.toString(), "onclick");
                    if (autoWidth){
                        String width = null;
                        if( i < tabsNum-1){
                            width = percentWidth;
                        }
                        else{
                            width = lastPercentWidth;
                        }
                        writer.writeAttribute(HTML.STYLE_ATTR, width, HTML.STYLE_ATTR);
                    }
                    String icon = cp.getIcon();
                    if( icon != null ){
                        writer.startElement("i", null);
                        writer.writeAttribute(HTML.CLASS_ATTR, "fa fa-" + icon, null);
                        writer.endElement("i");
                    }
                    String title = cp.getTitle();
                    writer.write(title);
                    writer.endElement(HTML.LI_ELEM);
                }
            }
        }
        writer.endElement(HTML.UL_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        IResponseWriter writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        TabSetCoreRenderer renderer = new TabSetCoreRenderer();
        ITabSet tabSet = (ITabSet) uiComponent;
        encodeHidden(facesContext, uiComponent, String.valueOf(tabSet.getIndex()));
        writer.endElement(HTML.DIV_ELEM);  //end of content div
        String orientation = tabSet.getOrientation();
        boolean bottom = TabSet.OrientationType.bottom.name().equals(orientation);
        if (bottom){
            encodeTabs(facesContext, uiComponent);
        }
        renderer.encodeScript(tabSet, writer, false);
        writer.endElement(HTML.DIV_ELEM);  //end of tabset container
    }

}