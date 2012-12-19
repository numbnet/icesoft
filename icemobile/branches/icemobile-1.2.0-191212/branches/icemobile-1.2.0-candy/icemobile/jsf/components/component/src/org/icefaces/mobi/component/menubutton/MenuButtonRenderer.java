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
package org.icefaces.mobi.component.menubutton;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.JSFUtils;


public class MenuButtonRenderer extends BaseLayoutRenderer {
    private static Logger logger = Logger.getLogger(MenuButtonRenderer.class.getName());
    private static final String JS_NAME = "menubutton.js";
    private static final String JS_MIN_NAME = "menubutton-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.button";


    public void decode(FacesContext facesContext, UIComponent uiComponent) {
         Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
         MenuButton menu = (MenuButton) uiComponent;
         String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
         String clientId = menu.getClientId();
         if (clientId.equals(source)) {
             try {
                 if (!menu.isDisabled()) {
                     /*  currently using submit of menuButtonItem
                     find the item in the list and queue the event for it*/
                 }
             } catch (Exception e) {
                 logger.warning("Error queuing MenuButtonItem event");
             }
         }
     }

     public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
             throws IOException {
         ResponseWriter writer = facesContext.getResponseWriter();
         String clientId = uiComponent.getClientId(facesContext);
         MenuButton menu = (MenuButton) uiComponent;
      	 UIComponent form = JSFUtils.findParentForm(uiComponent);
   		 if(form == null) {
			throw new FacesException("MenuButton : \"" + clientId + "\" must be inside a form element");
		 }
         // root element
         writeJavascriptFile(facesContext, uiComponent, JS_NAME, JS_MIN_NAME, JS_LIBRARY);
         writer.startElement(HTML.DIV_ELEM, uiComponent);
         writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
         writer.writeAttribute(HTML.NAME_ATTR, clientId, HTML.NAME_ATTR);
         // apply button type style classes
         StringBuilder baseClass = new StringBuilder(MenuButton.BASE_STYLE_CLASS);
         StringBuilder buttonClass = new StringBuilder(MenuButton.BUTTON_STYLE_CLASS) ;
         StringBuilder selectClass = new StringBuilder(MenuButton.MENU_SELECT_CLASS);
         String userDefinedClass = menu.getStyleClass();
         if (null != userDefinedClass) {
             baseClass.append(userDefinedClass);
             selectClass.append(userDefinedClass);
             buttonClass.append(userDefinedClass);
         }
         // apply disabled style state if specified.
         if (menu.isDisabled()) {
             baseClass.append(MenuButton.DISABLED_STYLE_CLASS);
         }
         writer.writeAttribute(HTML.CLASS_ATTR, baseClass.toString(), null);

         // should be auto base though
         writer.startElement(HTML.SPAN_ELEM, uiComponent);
         writer.writeAttribute(HTML.CLASS_ATTR, buttonClass.toString(), HTML.CLASS_ATTR);
         writer.writeAttribute(HTML.ID_ATTR, clientId+"_btn", HTML.ID_ATTR);
         writer.startElement(HTML.SPAN_ELEM, uiComponent);
         String selectLabel = menu.getButtonLabel();
         writer.write(selectLabel);
         writer.endElement(HTML.SPAN_ELEM);
         writer.endElement(HTML.SPAN_ELEM);
         if (menu.isDisabled()) {
             writer.writeAttribute("disabled", "disabled", null);
             writer.endElement(HTML.DIV_ELEM);
             return;
         }
         //setup config object
         writer.startElement(HTML.SELECT_ELEM, uiComponent);
         writer.writeAttribute(HTML.ID_ATTR, clientId+"_sel", HTML.ID_ATTR);
         writer.writeAttribute(HTML.NAME_ATTR, clientId+"_sel", HTML.NAME_ATTR);
         writer.writeAttribute(HTML.CLASS_ATTR, selectClass, HTML.CLASS_ATTR);
         writer.writeAttribute(HTML.ONCHANGE_ATTR, "mobi.menubutton.select('"+clientId+"');", HTML.ONCHANGE_ATTR);
         if (null!=menu.getStyle()){
             String style= menu.getStyle();
             if ( style.trim().length() > 0) {
                 writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);
             }
         }
         if (menu.getVar() != null) {
            menu.setRowIndex(-1);
            for (int i = 0; i < menu.getRowCount(); i++) {
                //assume that if it's a list of items then it's grouped
                menu.setRowIndex(i);
                // option can't have children tags but can be disabled ...not too sure what to do about that
                /* check to see that only child can be MenuButtonItem?  */
                renderChildren(facesContext, menu);
            }
            menu.setRowIndex(-1);
            writer.endElement(HTML.SELECT_ELEM);
        }  else {
             //doing it with indiv menuButtonItem tag's
             renderChildren(facesContext, menu);
             writer.endElement(HTML.SELECT_ELEM);
         }
         writer.endElement(HTML.DIV_ELEM);
         encodeScript(facesContext, uiComponent);
     }

    @Override
    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
         //Rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    public void encodeScript(FacesContext context, UIComponent uiComponent) throws IOException{
            //need to initialize the component on the page and can also
        ResponseWriter writer = context.getResponseWriter();
        MenuButton menu = (MenuButton) uiComponent;
        String clientId = menu.getClientId(context);
        writer.startElement("span", uiComponent);
        writer.writeAttribute("id", clientId+"_initScr", "id");
        writer.startElement("script", uiComponent);
        writer.writeAttribute("type", "text/javascript", null);
        StringBuilder sb = new StringBuilder("mobi.menubutton.initmenu('");
        sb.append(clientId).append("',").append("{ selectTitle: '").append(menu.getSelectTitle()).append("'});");
        writer.write(sb.toString());
         if (!menu.getMenuItemCfg().isEmpty()){
             for (Map.Entry<String, StringBuilder> entry: menu.getMenuItemCfg().entrySet()){
              //    logger.info(" item cfg prints="+entry.getValue().toString());
                 StringBuilder jsCall = new StringBuilder("mobi.menubutton.initCfg('").append(clientId).append("',");
                 jsCall.append(entry.getValue());
                  writer.write(jsCall.toString());
             }
         }
         writer.endElement("script");
         writer.endElement("span");
     }

}
