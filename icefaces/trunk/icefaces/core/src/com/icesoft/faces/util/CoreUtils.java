package com.icesoft.faces.util;

import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

public class CoreUtils {
	private static Boolean renderPortletStyleClass;
	private static Boolean portletEnvironment;
	
    public static String resolveResourceURL(FacesContext facesContext, String path) {
        return facesContext.getApplication().getViewHandler().getResourceURL(facesContext, path);
    }
    
    public static boolean isPortletEnvironment() {
    	if (portletEnvironment == null) {
		    try {
		    	portletEnvironment = new Boolean(FacesContext.getCurrentInstance().getExternalContext()
		    			.getRequest() instanceof javax.portlet.PortletRequest);
		    } catch (java.lang.NoClassDefFoundError e) {
		    	//portlet not found
		    	portletEnvironment = Boolean.FALSE;
			}
    	}
    	return portletEnvironment.booleanValue();
    }
    
    public static String getPortletStyleClass(String className) {
    	if (isPortletEnvironment() && isRenderPortletStyleClass()) {
    		return " "+ className;
    	}
    	return "";
    }
    
    private static boolean isRenderPortletStyleClass() {
    	if (renderPortletStyleClass == null) {
    		String renderStyle = FacesContext.getCurrentInstance().getExternalContext().
			 getInitParameter("com.icesoft.faces.portlet.renderStyles");
    		if (renderStyle == null) {
    			//default is true
    			renderPortletStyleClass = Boolean.TRUE;
    		} else {
    			renderPortletStyleClass = Boolean.valueOf(renderStyle);
    		}
    		
    	}
    	return renderPortletStyleClass.booleanValue();
    }
    
    public static String addPortletStyleClassToQualifiedClass(String qualifiedStyleClass, 
    																String defaultClass, 
    																String portletClass) {
    	return addPortletStyleClassToQualifiedClass(qualifiedStyleClass, 
    												defaultClass, 
    												portletClass, 
    												false);
    		 
    }

    public static String addPortletStyleClassToQualifiedClass(String qualifiedStyleClass, 
    															String defaultClass, 
    															String portletClass, 
    															boolean disabled) {
    	if (isPortletEnvironment() && isRenderPortletStyleClass()) {
	    	if (disabled) {
	    		return qualifiedStyleClass.replaceAll(defaultClass+"-dis", 
	    				defaultClass + "-dis" + " " + portletClass);
	    	} else {
	    		return qualifiedStyleClass.replaceAll(defaultClass, 
	    				defaultClass + " " + portletClass);
	    	}
    	}else {
    		return qualifiedStyleClass;
    	}
    }

    /*
     * This method will help to retain the faces messages on the partialSubmit, 
     * the page refresh and the dynamic component rendering.
     * It will be called by two classes the DomBasicRenderer and the MessageRenderer.
     * 
     * Calling it from the DomBasicRenderer insures that there will always be a 
     * message in the default messages Map, only if the component was invalid. 
     * So if the jsp document has an "ice" or "h" messages component they can 
     * serve the default messages map. 
     * 
     * Calling this method from the MessageRenderer gives the component ordering
     * flexibility. (e.g) It doesn't matter if the "INPUT" component was rendered 
     * first or the "message" component.
     * 
     * Calling this method from two renderer will not cause any problem or side effect.
     * The first calling class will complete the work, and the next one will already 
     * find a message and won't repeat the process.
     */
    public static void recoverFacesMessages(FacesContext facesContext, UIComponent uiComponent) {
        if (!(uiComponent instanceof UIInput)) return;
        UIInput input = (UIInput) uiComponent;
        String clientId = input.getClientId(facesContext);
        String localeFacesMsgId = clientId + "$ice-msg$";
        String localeRequired = clientId + "$ice-req$";  
        //save the required attribute, specifically for UIData
        if (input.getAttributes().get(localeRequired) == null) {
            // this property will be used by the UISeries.restoreRequiredAttribute()
            input.getAttributes().put(localeRequired,new Boolean(input.isRequired()));
        }
        //component is invalid there should be a message in the default messages map
        if (!input.isValid()) {
            Iterator messages = facesContext.getMessages(clientId);
            FacesMessage message = null;
            //if no message found, then it might a page refresh call
            //or the request of dynamic rendering of the component. 
            //if so, get the message from the component's map and add
            //it to the default messages map
            if (messages == null || !messages.hasNext()) {
                if(input.getAttributes().get(localeFacesMsgId) != null) {
                    message = (FacesMessage) input.getAttributes().get(localeFacesMsgId);
                    facesContext.addMessage(clientId, message);
                }
            } else {//if found, then store it to the component's message map,
                //so can be served later.
                message = (FacesMessage) messages.next();
                input.getAttributes().put(localeFacesMsgId,message );
            }
        } else { //component is valid, so remove the old message.
            input.getAttributes().remove(localeFacesMsgId);
        }        
    }
}
