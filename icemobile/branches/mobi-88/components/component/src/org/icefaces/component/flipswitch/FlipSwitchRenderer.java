/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icefaces.component.flipswitch;

import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.PassThruAttributeWriter;
import org.icefaces.component.utils.Utils;
import org.icefaces.render.MandatoryResourceComponent;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The sliderRender renders following elements:
 * 1. A div with a client id(e.g.)
 * <div id="xxx" />
 * which will be used by the YUI slider, as a slider holder
 * <p/>
 * In addition to the rendering the renderer performs decode as well. This component
 * doesn't use a hidden field for it value instead takes advantage of param support of JSF2
 */
@MandatoryResourceComponent("org.icefaces.component.flipswitch.FlipSwitch")
public class FlipSwitchRenderer extends Renderer {

    private final static Logger logger = Logger.getLogger(FlipSwitchRenderer.class.getName());

    // The decode method, in the renderer, is responsible for taking the values
    // 
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        // The RequestParameterMap holds the values received from the browser
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        String clientId = uiComponent.getClientId(facesContext);
        FlipSwitch flipswitch = (FlipSwitch) uiComponent;
        if (flipswitch.isDisabled()){
        	return;
        }
        //update with hidden field
        String submittedString = String.valueOf(requestParameterMap.get(clientId+"_hidden"));
        if (submittedString!=null){
            boolean submittedValue = isChecked(submittedString);
            flipswitch.setSubmittedValue(submittedValue);
        }
    }

    // The encodeEnd method, in the renderer, is responsible for rendering
    //  the html markup, as well as the javacript necessary for 
    //  the browser. Typically the encodeEnd(-)
    //  method and possibly the encodeChildren(-) method would be used too,
    //  but we've put all the rendering here, in this one method.
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        String clientId = uiComponent.getClientId(facesContext);
        ResponseWriter writer = facesContext.getResponseWriter();
        FlipSwitch flipswitch = (FlipSwitch) uiComponent;

        // capture any children UIParameter (f:param) parameters.
//        uiParamChildren = Utils.captureParameters( slider );

        // Write outer div
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, HTML.NAME_ATTR);
           
        writer.startElement(HTML.ANCHOR_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_anchor", HTML.ID_ATTR);
 //       writer.writeAttribute("href", "#", null);
        String styleClass = flipswitch.FLIPSWITCH_OFF_CLASS;
        String switchValue = String.valueOf(flipswitch.getValue());
        boolean isChecked = this.isChecked(switchValue);
        if (isChecked){
        	styleClass = flipswitch.FLIPSWITCH_ON_CLASS;
        }
        writer.writeAttribute("class", styleClass, "class");
        PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent, flipswitch.getAttributesNames());
        PassThruAttributeWriter.renderBooleanAttributes(writer, uiComponent, flipswitch.getBooleanAttNames());
        String labelOn=flipswitch.getLabelOn();
        String labelOff=flipswitch.getLabelOff();
        
 //       StringBuilder jsCall = this.writeJSCall(clientId, flipswitch);
        writer.writeAttribute("onclick", "mobi.flipper.init('"+clientId+"', event, this,"+flipswitch.isSingleSubmit()+");", null);
        
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute("class", "mobi-flip-switch-txt", null);
        writer.write(labelOn);
        writer.endElement(HTML.SPAN_ELEM);
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute("class", "mobi-flip-switch-txt", null);
        writer.write(labelOff);
        writer.endElement(HTML.SPAN_ELEM);
        writer.endElement(HTML.ANCHOR_ELEM);
        writeHiddenField(uiComponent, clientId, writer, switchValue);
        writer.endElement("input");

        writer.endElement(HTML.DIV_ELEM);
    }

	private void writeHiddenField(UIComponent uiComponent, String clientId,
			ResponseWriter writer, String switchValue) throws IOException {
		writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("name", clientId + "_hidden", null);
        writer.writeAttribute("id", clientId + "_hidden", null);
        writer.writeAttribute("value", switchValue, null);
	}
    private boolean isChecked(String hiddenValue) {
        return hiddenValue.equalsIgnoreCase("on") ||
                hiddenValue.equalsIgnoreCase("yes") ||
                hiddenValue.equalsIgnoreCase("true");
    }
    //forced converter support. It's either a boolean or string.   
    @Override
    public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent,
    		                        Object submittedValue) throws ConverterException{
    	if (submittedValue instanceof Boolean) {
            return submittedValue;
        }
    	else {
            return Boolean.valueOf(submittedValue.toString());
        }
    }
    
    /*
     * not currently being used.  Will switch to build in script if better performance.  For now leave
     * in separate javascript file as easier to modify/debug
     */
    private StringBuilder writeJSCall(String clientId, FlipSwitch flipper) {
        final StringBuilder script = new StringBuilder();
        //first update the hidden field.  If class is on class then switch class to off and update hidden field to off
        //otherwise, vice versa
        String hiddenField=clientId+"_hidden";
        script.append("var hidden = getElementById('"+hiddenField+"');"+
        		"if(this.className='"+flipper.FLIPSWITCH_ON_CLASS+"'){"+"" +
        		    "this.className='"+flipper.FLIPSWITCH_OFF_CLASS+";"+       		
        		    "if(hidden){hidden.value='on';}"+
        		"}"+
        		"else{"+
        		   "this.className='"+flipper.FLIPSWITCH_ON_CLASS+"';"+
        		   "if(hidden){hidden.value='off';}"+
        		"}");
        
        if (flipper.isSingleSubmit()){
        	script.append("ice.se(event, '" + clientId + "');");
        	//submit the div. as need the hidden field       
        }
      //  logger.info(" script is ="+script.toString());
        return script;
    }
	/**
     * will render it's own children
     */
	public boolean getRendersChildren() {
		return true;
	}
}
