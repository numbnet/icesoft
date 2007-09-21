package com.icesoft.faces.util;

import javax.faces.context.FacesContext;

public class CoreUtils {
	private static Boolean renderPortletStyleClass;
    public static String resolveResourceURL(FacesContext facesContext, String path) {
        return facesContext.getApplication().getViewHandler().getResourceURL(facesContext, path);
    }
    
    public static boolean isPortletEnvironment() {
	    try {
	    	return (FacesContext.getCurrentInstance().getExternalContext()
	    			.getRequest() instanceof javax.portlet.PortletRequest);
	    } catch (java.lang.NoClassDefFoundError e) {
	    	//portlet not found
	    	return false;
		}
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

}
