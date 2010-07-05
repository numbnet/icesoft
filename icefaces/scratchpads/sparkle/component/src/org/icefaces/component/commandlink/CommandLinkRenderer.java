package org.icefaces.component.commandlink;

import java.io.IOException;
import java.util.*;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;


import org.icefaces.component.utils.HTML;
import org.icefaces.util.EnvUtils;


public class CommandLinkRenderer extends Renderer {

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.containsKey("ice.event.captured")) {
            CommandLink link  = (CommandLink) uiComponent;
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
            String clientId = link.getClientId();
            uiComponent.queueEvent(new ActionEvent(uiComponent));
           if (clientId.equals(source)) {
        	   System.out.println("commandLink pressed is "+source+" for clientId="+clientId);
//               try {
//            	   //do I need to check to see if it is disabled first?
//            	   //ActionSource2 has a list of ActionListeners available
//            	   //will that change this piece of code?
//            	   if (!link.isDisabled()){
//                       uiComponent.queueEvent(new Event(uiComponent));
//            	   }
//               } catch (Exception e) {}
           }
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {

        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);

        CommandLink commandLink = (CommandLink) uiComponent;
		System.out.println("in renderer is instanceof? " + uiComponent.getClass() + " instanceof? " + (uiComponent instanceof CommandLink) );
		// root element

        writer.startElement(HTML.DIV_ELEM, uiComponent );
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);

        //writer.startElement(HTML.INPUT_ELEM, uiComponent);
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_span", null);
		writer.writeAttribute(HTML.CLASS_ATTR, "yui-button yui-link-button", null);

		// first child
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
		writer.writeAttribute(HTML.CLASS_ATTR, "first-child", null);
		writer.writeAttribute(HTML.ID_ATTR, "first-child", null);

		// button element
        String temp;
		writer.startElement(HTML.ANCHOR_ELEM, uiComponent);

        // Uncomment this for the so - called inline model onclick handler 
//        writer.writeAttribute(HTML.ONCLICK_ATTR, "return ice.component.commandlink.actionClickHandlerFullDeal(event)", null);

//        ActionListener[] al =  commandLink.getActionListeners();
//        boolean doAction = (al.length > 0);
//		writer.writeAttribute(HTML.NAME_ATTR, clientId, null);

//        if (doAction) {
//            writer.writeAttribute(HTML.HREF_ATTR, "#", null);
//        } else

        if ((temp = commandLink.getHref()) != null) {
            writer.writeAttribute(HTML.HREF_ATTR, temp, null );
        }

		// if there's an image, render label manually, don't rely on YUI, since it'd override button's contents
//		if (commandLink.getImage() == null) {
//			writer.startElement(HTML.SPAN_ELEM, uiComponent);
//			writer.write(pushButton.getLabel());
//			writer.endElement(HTML.SPAN_ELEM);
//		}else {
//			writer.startElement(HTML.IMG_ELEM, uiComponent);
//			writer.writeAttribute(HTML.SRC_ATTR, pushButton.getImage(), null);
//			writer.endElement(HTML.IMG_ELEM);
//		}
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
		String clientId = uiComponent.getClientId(facesContext);
        
		CommandLink commandLink = (CommandLink) uiComponent;
        writer.endElement(HTML.ANCHOR_ELEM);
		writer.endElement(HTML.SPAN_ELEM);
		writer.endElement(HTML.SPAN_ELEM);
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_js", null);


        StringBuilder call= new StringBuilder();
        call.append("ice.component.commandlink.updateProperties('");
        call.append(clientId);
        call.append("', ");
        //pass through YUI  properties
        call.append("{");
        call.append("type:'button'");
//        if (pushButton.getImage() == null) {
//            call.append(", label:'");
//            call.append(pushButton.getLabel());
//            call.append("'");
//        }

        call.append("},");
        //pass JSF component specific properties that would help in slider configuration
        call.append("{");
        call.append("singleSubmit:");
        call.append(commandLink.isSingleSubmit());


        // if doAction is true, we mustn't execute the default action
        ActionListener[] al =  commandLink.getActionListeners();
        boolean doAction = (al.length > 0);

        System.out.println("Value of doAction = "+ doAction);
        call.append(", ");
        call.append("aria:");
        call.append(EnvUtils.isAriaEnabled(facesContext));
        call.append(", ");
        call.append("tabindex:");
        call.append(commandLink.getTabindex());
        call.append(", ");
        call.append("doAction:");
        call.append(doAction);
        call.append("});");

        writer.startElement(HTML.SCRIPT_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "script", HTML.ID_ATTR);
        writer.write(call.toString());
        writer.endElement(HTML.SCRIPT_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
        
        writer.endElement(HTML.DIV_ELEM);
    }
}