package com.icefaces.project.memory.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Utility class used to handle FacesContext related methods.
 * Examples would be getting parameters, adding error messages, etc.
 */
public class FacesUtil {
	/**
	 * Method to wrap the passed text in a valid FacesMessage object
	 * 
	 * @param errorText to wrap
	 * @return resulting message
	 */
	public static FacesMessage generateMessage(String errorText) {
		return new FacesMessage(FacesMessage.SEVERITY_ERROR, errorText, errorText);
	}
	
	/**
	 * Method to add the passed text as a global error message
	 * 
	 * @param errorText to output
	 */
	public static void addGlobalMessage(String errorText) {
		// Passing a null id means put this as a global message
		FacesContext.getCurrentInstance().addMessage(null, generateMessage(errorText));		
	}
	
	/**
	 * Method to get a managed bean from the context
	 * This approach is not ideal, as injection should be used instead
	 * But in some cases (ie: validators and converters) there is no other way to get at a bean
	 * 
	 * @param beanName to get
	 * @return the bean
	 */
	public static Object getManagedBean(String beanName) {
        FacesContext context = FacesContext.getCurrentInstance();
        
        return context.getApplication().createValueBinding("#{" + beanName + "}").getValue(context);
	}
	
	/**
	 * Method to return the value of an f:attribute tag, based on the passed name
	 * 
	 * @param event that has the f:attribute associated with it
	 * @param name of the tag
	 * @return value of the tag
	 */
    public static Object getFAttribute(ActionEvent event, String name) {
        return (Object)event.getComponent().getAttributes().get(name);
    }
    
    /**
     * Method to get a parameter from the HTTP request for the passed key
     *
     * @param key of the parameter
     * @return the parameter, or null on error or if not found
     */
    public static String getURLParameter(String key) {
        try{
            HttpServletRequest req = getCurrentRequest();
            return req.getParameter(key);
        }catch (Exception paramError) {
        	paramError.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Method to safely get the current HTTP request
     * 
     * @return request, or null on error
     */
    public static HttpServletRequest getCurrentRequest() {
        try{
            return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        }catch (Exception requestError) {
        	requestError.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Method to safely get the current HTTP response
     * 
     * @return response, or null on error
     */    
    public static HttpServletResponse getCurrentResponse() {
        try{
        	return (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
        }catch (Exception responseError) {
        	responseError.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Method to get a context-param value from the web.xml, based on the passed name
     * 
     * @param parameter name to get
     * @return value of the parameter, or null if not found
     */
    public static String getContextParameter(String parameter) {
        try{
            // Get the servlet context based on the faces context
            ServletContext sc = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
            
            // Return the value read from the parameter
            return sc.getInitParameter(parameter);
        }catch (Exception failedParameter) {
        	failedParameter.printStackTrace();
        }
        
        return null;
    }
	
    /**
     * Wrapper method to grab the running path of the web application
     * This would normally point to the deploy directory of the app server we are on
     * This is useful if we need to get a file deeper inside the project structure, but can't use relative paths
     * 
     * @return base filesystem directory
     * @throws Exception on error
     */
    public static String getBaseFilesystemDir() throws Exception {
        return ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/");
    }
    
    /**
     * Wrapper method the same as getBaseFilesystemDir, except we return "." (current dir)
     *  on error or failure
     *  
     * @return base filesystem directory, or "." on error
     */
    public static String getSafeBaseFilesystemDir() {
        try{
            return getBaseFilesystemDir();
        }catch (Exception ignored) { }
        
        return ".";
    }
}
