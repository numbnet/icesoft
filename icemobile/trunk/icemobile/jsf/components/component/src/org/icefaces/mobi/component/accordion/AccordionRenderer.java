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

package org.icefaces.mobi.component.accordion;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.JSFUtils;
import org.icefaces.mobi.utils.MobiJSFUtils;


public class AccordionRenderer extends BaseLayoutRenderer {
    private static Logger logger = Logger.getLogger(AccordionRenderer.class.getName());
    private static final String JS_NAME = "accordion.js";
    private static final String JS_MIN_NAME = "accordion-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.accordion";

    @Override
    public void decode(FacesContext context, UIComponent component) {
         Accordion accordion = (Accordion) component;
         String clientId = accordion.getClientId(context);
         Map<String, String> params = context.getExternalContext().getRequestParameterMap();
       // no ajax behavior defined yet
         String indexStr = params.get(clientId + "_hidden");
         if( null != indexStr) {
             String oldId = accordion.getCurrentId();
             String newId = JSFUtils.getIdOfChildByClientId(context, accordion, indexStr);
             if (newId != null && !newId.equals(oldId)) {
                 accordion.setCurrentId(newId);
                 component.queueEvent(new ValueChangeEvent(component, oldId, newId));
             }
         }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Accordion accordion = (Accordion) uiComponent;
        writeJavascriptFile(facesContext, uiComponent, JS_NAME, JS_MIN_NAME, JS_LIBRARY);
        /* write out root tag.  For current incarnation html5 semantic markup is ignored */
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        // apply default style class
        StringBuilder styleClass = new StringBuilder(accordion.ACCORDION_CLASS);
        // user specified style class
        String userDefinedClass = accordion.getStyleClass();
        if (userDefinedClass != null && userDefinedClass.length() > 0){
                styleClass.append(" ").append(userDefinedClass);
        }
        writer.writeAttribute("class", styleClass.toString(), "styleClass");
        // write out any users specified style attributes.
        writer.writeAttribute(HTML.STYLE_ATTR, accordion.getStyle(), "style");
        // need to set opened panel
    //    encodeDataOpenedAttribute(facesContext, uiComponent);
    }

    public boolean getRendersChildren() {
        return true;
    }
    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException{
         JSFUtils.renderChildren(facesContext, uiComponent);
    }
    public String getDataOpenedAttribute(FacesContext facesContext, UIComponent uiComponent) {
        Accordion paneController = (Accordion) uiComponent;
        UIComponent openPane = null;  //all children must be panels
        String currentId = paneController.getCurrentId();

        if (paneController.getChildCount() <= 0){
                 // || logger.isLoggable(Level.FINER)) {
            logger.finer("this component must have panels defined as children. Please read DOCS.");
                return null;
        } //check whether we have exceeded maximum number of children for accordion???
        openPane = JSFUtils.getChildById(paneController, currentId);
 //       logger.info("looking for index="+activeIndex+" selectedPane to open ="+openPane.getId());
        //selectedPanel is now set
        String clId = null;
        if (openPane !=null){
            clId = openPane.getClientId(facesContext);
        }
        return clId;
    }

     public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException {
         ResponseWriter writer = facesContext.getResponseWriter();
         encodeHidden(facesContext, uiComponent);
         writer.endElement(HTML.DIV_ELEM);
         encodeScript(facesContext, uiComponent);
     }

     public void encodeScript(FacesContext context, UIComponent uiComponent) throws IOException {
        //need to initialize the component on the page and can also
        ResponseWriter writer = context.getResponseWriter();
        Accordion pane = (Accordion) uiComponent;
        String clientId = pane.getClientId(context);
        writer.startElement("span", uiComponent);
        writer.writeAttribute("id", clientId + "_script", "id");
        writer.startElement("script", null);
        writer.writeAttribute("text", "text/javascript", null);
        String paneOpened = getDataOpenedAttribute(context, uiComponent);
        StringBuilder cfg = new StringBuilder("{singleSubmit: false");
        if (null!=paneOpened){
            cfg.append(", opened: '").append(paneOpened).append("'");
        }
        boolean autoheight = pane.isAutoHeight();
        int hashcode = MobiJSFUtils.generateHashCode(paneOpened);
        cfg.append(", hash: ").append(hashcode);
        cfg.append(", autoheight: ").append(autoheight);
        cfg.append(", maxheight: '").append(pane.getFixedHeight()).append("'");
        cfg.append("}");
         //just have to add behaviors if we are going to use them.
        writer.write("mobi.accordionController.initClient('" + clientId + "'," +cfg.toString()+");");
        writer.endElement("script");
        writer.endElement("span");
    }
}
