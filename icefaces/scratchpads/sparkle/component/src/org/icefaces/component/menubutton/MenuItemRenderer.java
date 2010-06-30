package org.icefaces.component.menubutton;

import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.event.ActionEvent;

import org.icefaces.component.utils.HTML;

public class MenuItemRenderer extends Renderer {

	private final static Logger log = Logger.getLogger(MenuItemRenderer.class.getName());
    
	public void decode(FacesContext facesContext, UIComponent uiComponent) {
 		System.out.println("MIR: decode");
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
       System.out.println("param map="+requestParameterMap);
        if (requestParameterMap.containsKey("ice.event.captured")) {
            MenuItem menuItem = (MenuItem) uiComponent;
    		MenuButton menuButton = (MenuButton)uiComponent.getParent();
    		String menuButtonId= menuButton.getClientId();
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
            String clientId = uiComponent.getClientId()+":menubutton";
            String id=this.getMenuItemId(facesContext, uiComponent);
             System.out.println("id ="+id+" menubuttonId="+menuButtonId);
            String decodedValue =
                ((String) requestParameterMap.get(menuButtonId+"_value")).trim();
            	System.out.println("\t\t decodedValue="+decodedValue);
             if (id.equals(decodedValue)) {
            	 System.out.println("MIR: queued event for id="+id);
                 uiComponent.queueEvent(new ActionEvent(uiComponent));			
             }
 
        }
	}
	//since yui controls the widget and you don't get a usable clientId
	//need to put together an id for this menuItem component
	//should this go in the component class or stay here??
	private String getMenuItemId(FacesContext fc, UIComponent uiComponent){
		MenuButton menuButton = (MenuButton)uiComponent.getParent();
		return menuButton.getClientId(fc) + ":"+ uiComponent.getId();
	}

	@Override
	public void encodeBegin(FacesContext facesContext, UIComponent uiComponent){ 
		MenuItem menuItem = (MenuItem) uiComponent;
		//if parent property= overlay (default) then we are looking at string values
		MenuButton parent = (MenuButton) uiComponent.getParent();
		String itemId = this.getMenuItemId(facesContext, uiComponent);
	    ResponseWriter writer = facesContext.getResponseWriter();
	    try{
		   if (!parent.isOverlay()){
			   writer.startElement(HTML.OPTION_ELEM, uiComponent);
			   writer.writeAttribute("value", itemId , null);
			   writer.writeText(menuItem.getLabel(), null);
			   
//			writer.write("<option value='"+menuItem.getValue().toString()+"'>"+menuItem.getLabel()+" </option>\n");
		}
	    }catch (Exception e){
	    	log.info("exception with trace");
	    	e.printStackTrace();
	    }
		//there really is no child that I can see
	}
	
	@Override
	public void encodeEnd(FacesContext facesContext, UIComponent uiComponent){
		MenuItem menuItem = (MenuItem) uiComponent;
		//if parent property= overlay (default) then we are looking at string values
	    ResponseWriter writer = facesContext.getResponseWriter();
	    
	    try{
	       writer.endElement(HTML.OPTION_ELEM);
	    }catch (Exception e){
	    	log.info("some kind of exception");
	    }
	}
	
}
