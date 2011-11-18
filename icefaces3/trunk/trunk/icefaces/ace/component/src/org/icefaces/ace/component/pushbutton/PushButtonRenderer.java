/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.ace.component.pushbutton;

import java.io.IOException;
import java.util.*;
import javax.faces.event.ActionEvent;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;


import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.ace.util.ScriptWriter;
import org.icefaces.ace.util.Utils;
import org.icefaces.util.EnvUtils;
import org.icefaces.render.MandatoryResourceComponent;


@MandatoryResourceComponent("org.icefaces.ace.component.pushbutton.PushButton")
public class PushButtonRenderer extends Renderer {

    List <UIParameter> uiParamChildren;

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
    	Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
            PushButton pushButton = (PushButton) uiComponent;
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
            String clientId = pushButton.getClientId();
  
             if (clientId.equals(source)) { 
                try {
             	   if (!pushButton.isDisabled()){
                        uiComponent.queueEvent(new ActionEvent (uiComponent));

             	   }
                } catch (Exception e) {}
             }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        PushButton pushButton = (PushButton) uiComponent;

          // capture any children UIParameter (f:param) parameters.
        uiParamChildren = Utils.captureParameters( pushButton );

		// root element
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
		writer.writeAttribute(HTML.CLASS_ATTR, "ice-pushbutton", null);
        
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_span", null);
        String yuiBaseClass= "yui-button yui-min yui-push-button";
        Object styleClass = pushButton.getStyleClass();
        if (null!=styleClass){
             yuiBaseClass +=  " " + String.valueOf(styleClass);
        }
		writer.writeAttribute(HTML.STYLE_CLASS_ATTR, yuiBaseClass, null);
        String style = pushButton.getStyle();
        if (style != null && style.trim().length() > 0) {
            writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);
        }
		// first child
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
		writer.writeAttribute(HTML.CLASS_ATTR, "first-child", null);
	 	writer.writeAttribute(HTML.ID_ATTR, clientId+"_s2", null);
	 	
		// button element
		writer.startElement(HTML.BUTTON_ELEM, uiComponent);
		writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
        // ICE-6418 Write the id out to be the same as the eventual munged YUI id.
		writer.writeAttribute(HTML.NAME_ATTR, clientId+"_button", null);
        // ICE-6418 Don't define id's where unnecessary
//		writer.writeAttribute(HTML.ID_ATTR, clientId+"_span-button", null);
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
		Object oVal = pushButton.getValue();
		if (null!=oVal) writer.writeText(String.valueOf(oVal), null);
		else{
            String label = pushButton.getLabel();
            if (label != null) {
                writer.writeText(label, null);
            }
		}
		writer.endElement(HTML.SPAN_ELEM);
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
		String clientId = uiComponent.getClientId(facesContext);
		PushButton pushButton = (PushButton) uiComponent;
        writer.endElement(HTML.BUTTON_ELEM);
		writer.endElement(HTML.SPAN_ELEM);
		writer.endElement(HTML.SPAN_ELEM);
		 
		// js call using JSONBuilder utility ICE-5831 and ScriptWriter ICE-5830
	    String ariaLabel = "";
	    String yuiLabel="";
	    String builder = "";
	    //if there is a value, then it goes to yui-label property
	    //otherwise, see if there is a label property.  If both exist then send 
	    //separately.
	    Object oVal=pushButton.getValue();
	    if (null!=oVal) yuiLabel = String.valueOf(oVal);
	    Object oLab=pushButton.getLabel();
	    if (null!=oLab) ariaLabel=String.valueOf(oLab);
	    if (yuiLabel.equals(""))yuiLabel=ariaLabel;
	    if (ariaLabel.equals(""))ariaLabel=yuiLabel;

        boolean ariaEnabled = EnvUtils.isAriaEnabled(facesContext);
        Integer tabindex = pushButton.getTabindex();
        if (ariaEnabled && tabindex == null) tabindex = 0;

        JSONBuilder jBuild = JSONBuilder.create().beginMap();
        // need to worry if label isn't set?
	    jBuild.
	    entry("type", "button").
        entry("disabled", pushButton.isDisabled());
        if (tabindex != null) {
            jBuild.entry("tabindex", tabindex);
        }
        builder = jBuild.entry("label", yuiLabel).endMap().toString();

        StringBuilder sb = new StringBuilder();
        sb.append( pushButton.getValue() ).
                append(pushButton.getStyleClass()).
                append(pushButton.getStyle());

        jBuild = JSONBuilder.create().
                                beginMap().
                entry("singleSubmit", pushButton.isSingleSubmit()).
                entry("ariaLabel", ariaLabel).
                entry("hashCode",  sb.toString().hashCode()).
                entry("ariaEnabled", ariaEnabled);

        if (uiParamChildren != null) {
            jBuild.entry("postParameters",  Utils.asStringArray(uiParamChildren) );
        }

	    String params = "'" + clientId + "'," +
        builder
           + "," + jBuild.endMap().toString();
 //         System.out.println("params = " + params);	    

        String finalScript = "ice.component.pushbutton.updateProperties(" + params + ");";
        ScriptWriter.insertScript(facesContext, uiComponent,finalScript);
            
        writer.endElement(HTML.DIV_ELEM);
    }
}
