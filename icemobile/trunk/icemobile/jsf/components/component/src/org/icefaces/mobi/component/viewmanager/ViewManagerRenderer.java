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
package org.icefaces.mobi.component.viewmanager;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlBody;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.JSFUtils;

public class ViewManagerRenderer  extends BaseLayoutRenderer {
	
	private static final Logger LOG = Logger.getLogger(ViewManagerRenderer.class.getName());

    @Override
    public void decode(FacesContext context, UIComponent component) {

    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        ViewManager vm = (ViewManager)uiComponent;
        validate(vm);
        
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = vm.getClientId();
        
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        String styleClass = ViewManager.STYLECLASS;
        String userClass = vm.getStyleClass();
        if( userClass != null )
            styleClass += " " + userClass;
        writer.writeAttribute(HTML.CLASS_ATTR, styleClass, "styleClass");
        String userStyle = vm.getStyle();
        if( userStyle != null )
            writer.writeAttribute(HTML.STYLE_ATTR, userStyle, "style");
        
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "mobi-vm-header", null);
        writer.startElement(HTML.H1_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_title", "pageTitle");
        writer.write(vm.getTitle());
        writer.endElement(HTML.H1_ELEM);
        writer.endElement(HTML.DIV_ELEM);
        
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_menu", null);
        writer.startElement(HTML.UL_ELEM, null);
        List<UIComponent> children = vm.getChildren();
        for( UIComponent child : children ){
        	View view = (View)child;
            writer.startElement(HTML.LI_ELEM, null);
            writer.startElement(HTML.ANCHOR_ELEM, null);
            writer.writeAttribute(HTML.ONCLICK_ATTR, "ice.mobi.viewManager.showPage('" + view.getId() + "');", null);
            if( view.getMenuIcon() != null ){
                writer.startElement("i", null);
                writer.writeAttribute(HTML.CLASS_ATTR, view.getMenuIcon(), null);
                writer.endElement("i");
            }
            writer.endElement(HTML.ANCHOR_ELEM);
            writer.endElement(HTML.LI_ELEM);
        }
        writer.endElement(HTML.UL_ELEM);
        writer.endElement(HTML.DIV_ELEM);
       
    	
       /*
        * <div class="header">
        *    <h1 id-"pageTitle"></h1>
        * </div>
        * <div id="splash">
        * 
        * </div>
        * <div id="menu">
        *    <ul>
        *       <li><a onclick='showPage()'><i class='icon-xxx'></i>
        *    </ul>
        * </div>
        * <div id="page1">
        *    ...page content
        * </div>
        * <div id="imageCache"></div>
        */
    }

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        JSFUtils.renderChildren(facesContext, uiComponent);
    }


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
    
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, "mobiImageCache", null);
        writer.endElement(HTML.DIV_ELEM);
    	writer.endElement(HTML.DIV_ELEM);
    }
    
    private void validate(ViewManager vm){
        //parent must be the <h:body> compoent
        if( ! (vm.getParent() instanceof HtmlBody)){
           throw new IllegalStateException("The viewManager component must be the direct child of the <h:body> tag");
        }
    }

}