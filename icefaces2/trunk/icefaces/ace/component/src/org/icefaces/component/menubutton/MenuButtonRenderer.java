package org.icefaces.component.menubutton;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import javax.faces.render.Renderer;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.JSONBuilder;
import org.icefaces.component.utils.ScriptWriter;
import org.icefaces.util.EnvUtils;
import org.icefaces.render.MandatoryResourceComponent;


@MandatoryResourceComponent("org.icefaces.component.menubutton.MenuButton")
public class MenuButtonRenderer extends Renderer {
	private final static Logger log = Logger.getLogger(MenuButtonRenderer.class.getName());
    
	public void decode(FacesContext facesContext, UIComponent uiComponent) {
//is there value in placing the selectedMenuItem here???
		System.out.println("MBR decode");
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.containsKey("ice.event.captured")) {
        	//should never happen in simple scenario (?)
            MenuButton menuButton = (MenuButton) uiComponent;
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        MenuButton menuButton = (MenuButton) uiComponent;
        System.out.println("before anything menuButton id="+menuButton.getId());
        String outerDivClass = "yui-button .yui-menu-button";
        String typeAttribute = "button";  //default behaviour
        if (menuButton.isOverlay()){
        	outerDivClass="yui-overlay";
        }
		// root element is div which will be submitted
        writeOuterDivAndFirstChildSpan(uiComponent, writer, clientId,
				outerDivClass);
		// button element
		writeButton(uiComponent, writer, clientId, menuButton, typeAttribute);	
    }

 
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
    	log.info("encodeEnd");
        ResponseWriter writer = facesContext.getResponseWriter();
		String clientId = uiComponent.getClientId(facesContext);
		MenuButton menuButton = (MenuButton) uiComponent;
	    writer.endElement("select");
		writer.endElement(HTML.SPAN_ELEM);
		
		//hidden field--not sure this is needed yet???
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
	    writer.writeAttribute("type", "hidden", null);
	    writer.writeAttribute("name",clientId+"_hidden", null);
	    writer.writeAttribute("id",clientId+"_hidden", null);
	    writer.writeAttribute("value", menuButton.getSelectedMenuItem(), null);		
		writer.endElement(HTML.SPAN_ELEM);
	
 		writer.startElement(HTML.SPAN_ELEM, uiComponent);
 		writeScriptTag(uiComponent, writer, clientId, menuButton, facesContext);
    }

	private void writeOuterDivAndFirstChildSpan(UIComponent uiComponent,
			ResponseWriter writer, String clientId, String outerDivClass)
			throws IOException {
		writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);      
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_span", null);    
		writer.writeAttribute(HTML.CLASS_ATTR, outerDivClass, null);
       //first child not appropriate for this type of markup when
	   // at least not using overlay

	}
    /**
     * 
     * @param uiComponent
     * @param writer  
     * @param clientId of this component
     * @param menuButton to write markup for
     * @param typeAttribute in this case "button"
     * @throws IOException
     */
	private void writeButton(UIComponent uiComponent, ResponseWriter writer,
			String clientId, MenuButton menuButton, String typeAttribute)
			throws IOException {
		writer.startElement("input", uiComponent);
		writer.writeAttribute("type", typeAttribute , null);
		writer.writeAttribute("name", clientId+"_button", null);
		writer.writeAttribute(HTML.ID_ATTR, clientId+"_button", null);
        writer.writeAttribute("value", menuButton.getLabel(), "value");
		writer.endElement("input");
	    writer.startElement("select", uiComponent);
	    writer.writeAttribute(HTML.ID_ATTR, clientId+"_buttonselect", null);
	    writer.writeAttribute("name", clientId +"_buttonselect", null);

		
		// if there's an image, render label manually, don't rely on YUI, since it'd override button's contents
//		if (menuButton.getImage() != null) {
//			writer.startElement(HTML.SPAN_ELEM, uiComponent);
//			writer.write(checkboxButton.getLabel());
//			writer.endElement(HTML.SPAN_ELEM);
//			
//			writer.startElement(HTML.IMG_ELEM, uiComponent);
//			writer.writeAttribute(HTML.SRC_ATTR, menuButton.getImage(), null);
//			writer.endElement(HTML.IMG_ELEM);
//		}
	}


	
	private void writeScriptTag(UIComponent uiComponent, ResponseWriter writer,
			String clientId, MenuButton menuButton,
			FacesContext facesContext) throws IOException {

		writer.writeAttribute(HTML.CLASS_ATTR, "first-child", null);
	 	writer.writeAttribute(HTML.ID_ATTR, clientId+"_s2", null);
		//script tag and surrounding span
	
		// js call
	    String params = "'" + clientId + "'," +
           JSONBuilder.create().
           beginMap().
               entry("type", "menu").
               entry("menu", clientId+"_buttonselect").
           endMap().toString() 
           + "," +
           JSONBuilder.create().
           beginMap().
               entry("disabled", menuButton.isDisabled()).
               entry("tabindex", menuButton.getTabindex()).
               entry("singleSubmit", menuButton.isSingleSubmit()).
               entry("ariaEnabled", EnvUtils.isAriaEnabled(facesContext)).
           endMap().toString();
          System.out.println("params = " + params);	    

        String finalScript = "ice.component.menubutton.updateProperties(" + params + ");";
        ScriptWriter.insertScript(facesContext, uiComponent, finalScript);
	 	
        writer.endElement(HTML.SPAN_ELEM);
	}
}
