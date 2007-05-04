package com.icesoft.icefaces.samples.jspStore;

import javax.faces.event.ActionEvent;
import javax.faces.context.*;
import com.icesoft.faces.async.render.*;
import com.icesoft.faces.webapp.xmlhttp.*;
import javax.faces.application.Application;

/**
 * Request scope bean to distinguish two separate ICEfaces includes
 * so that requestRender() will be performed on each. Each bean is added
 * via JSF binding to store's onDemandRenderer.
 */
public class RenderBean implements Renderable {
	    
	// callback to Store
	private Store storeBean;
	
	/**
	 * Provides a bare-bones binding to the page so that the
	 * RenderBean is instantiated.
	 * 
	 * @return null
	 */
	public String getDummy(){
		return null;
	}
	
	/**
	 * Renderable Interface
	 */
	private PersistentFacesState state;
	
	 /**
	  * Get the PersistentFacesState.
	  * 
	  * @return state the PersistantFacesState
	  */
	public PersistentFacesState getState(){
		return state;
	}
	
	/**
	 * Handles rendering exceptions for the progress bar.
	 * 
	 * @param renderingException the exception that occured
	 * 
	 */
	public void renderingException(RenderingException renderingException){
		renderingException.printStackTrace();
	}
	
	/**
	 * Default constructor adds the bean to store's onDemandRenderer.
	 */
	public RenderBean() {
		// get the JSF binding to the store bean
	    Application application = FacesContext.getCurrentInstance().getApplication();
	        Store storeBean = ((Store) application.createValueBinding("#{store}").
	                        getValue(FacesContext.getCurrentInstance()));
		
	    // used for rendering
		state = PersistentFacesState.getInstance();
		
		// add the bean to store's onDemandRenderer
		storeBean.addRenderBean(this);
		
	}
}
