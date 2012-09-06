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

package org.icefaces.mobi.component.contentstackmenu;

import org.icefaces.mobi.component.accordion.Accordion;
import org.icefaces.mobi.component.contentmenuitem.ContentMenuItem;
import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.Utils;
import org.w3c.dom.html.HTMLDivElement;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class  ContentStackMenuRenderer extends BaseLayoutRenderer {

    private static Logger logger = Logger.getLogger(ContentStackMenuRenderer.class.getName());
    private static final String JS_NAME = "layoutmenu.js";
    private static final String JS_MIN_NAME = "layoutmenu-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.layoutmenu";
    // may also need the js files for accordion if accordion attribute is true
    private static final String JS_ACC_NAME= "accordion.js";
    private static final String JS_ACC_MIN_NAME = "accordion-min.js";
    private static final String JS_ACC_LIB = "org.icefaces.component.accordion";


  /*  public void decode(FacesContext facesContext, UIComponent uiComponent) {
         Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
         ContentStackMenu menu = (ContentStackMenu) uiComponent;
         String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
         Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
         String clientId = menu.getClientId();
         String inputStr = params.get(clientId + "_hidden");
         if( null != inputStr) {
             //find the activeIndex and set it
             //going to have to validate on client....
         }
    } */
     @Override
     public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
             throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        ContentStackMenu menu = (ContentStackMenu) uiComponent;
      	UIComponent form = Utils.findParentForm(uiComponent);
        boolean accordion = menu.isAccordion();
   		if(form == null) {
			throw new FacesException("ContentStackMenu : \"" + clientId + "\" must be inside a form element");
		}
         // root element
         String userDefinedClass = menu.getStyleClass();
         if (accordion){
             writeJavascriptFile(facesContext, uiComponent, JS_NAME, JS_MIN_NAME, JS_LIBRARY,
                     JS_ACC_NAME, JS_ACC_MIN_NAME, JS_ACC_LIB);
         } else {
             writeJavascriptFile(facesContext, uiComponent, JS_NAME, JS_MIN_NAME, JS_LIBRARY);
         }
         writer.startElement(HTML.DIV_ELEM, uiComponent);
         writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
         writer.writeAttribute(HTML.NAME_ATTR, clientId, HTML.NAME_ATTR);
         // apply button type style classes
         StringBuilder baseClass = new StringBuilder(ContentStackMenu.LAYOUTMENU_CLASS);
         StringBuilder listClass = new StringBuilder(ContentStackMenu.LAYOUTMENU_LIST_CLASS);
         StringBuilder baseAccordionClass = new StringBuilder(ContentStackMenu.LAYOUTMENU_CLASS+"mobi-accordion");
         if (null != userDefinedClass) {
             baseClass.append(userDefinedClass);
             listClass.append(userDefinedClass);
             baseAccordionClass.append(" ").append(userDefinedClass);
         }
         if (menu.getStyle() !=null){
             writer.writeAttribute("style", menu.getStyle(), "style");
         }
         if (accordion){
             writer.writeAttribute("class",baseAccordionClass.toString(), null);
         }   else {
             writer.writeAttribute(HTML.CLASS_ATTR, baseClass.toString(), null);
             writer.startElement(HTML.UL_ELEM, uiComponent);
             writer.writeAttribute(HTML.CLASS_ATTR, listClass.toString(), HTML.CLASS_ATTR);
         }
         if (menu.getVar() != null) {
            menu.setRowIndex(-1);
            for (int i = 0; i < menu.getRowCount(); i++) {
                //assume that if it's a list of items then it's grouped
                menu.setRowIndex(i);
                // option can't have children tags but can be disabled ...not too sure what to do about that
                /* check to see that only child can be ContentMenuItem?  */
                renderChildren(facesContext, menu);
            }
            menu.setRowIndex(-1);
        }  else {
             //doing it with indiv ContentMenuItem tag's
             renderChildren(facesContext, menu);
         }
        writer.endElement(HTML.UL_ELEM);
        this.encodeHidden(facesContext, uiComponent);
        writer.endElement(HTML.DIV_ELEM);
   /*     writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.writeAttribute("type", "text", "type");
        writer.writeAttribute(HTML.VALUE_ATTR, " end of menu comp", HTML.VALUE_ATTR);
        writer.endElement(HTML.INPUT_ELEM);      */

    }
    @Override
    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        ContentStackMenu menu = (ContentStackMenu) component;
        writer.endElement(HTML.DIV_ELEM);
    /*    writer.startElement(HTML.INPUT_ELEM, component);
        writer.writeAttribute("type", "text", "type");
        writer.writeAttribute(HTML.VALUE_ATTR, " encode end of menu comp", HTML.VALUE_ATTR);
        writer.endElement(HTML.INPUT_ELEM); */
        if (menu.isAccordion()){
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


}
