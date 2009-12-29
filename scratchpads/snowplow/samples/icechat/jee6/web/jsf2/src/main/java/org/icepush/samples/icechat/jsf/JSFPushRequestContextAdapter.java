package org.icepush.samples.icechat.jsf;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icepush.samples.icechat.beans.model.AbstractPushRequestContext;

@Named(value="pushRequestContext")
@RequestScoped
public class JSFPushRequestContextAdapter extends AbstractPushRequestContext{	
	
	
	public JSFPushRequestContextAdapter(){
		FacesContext fc = FacesContext.getCurrentInstance();
		intializePushContext((HttpServletRequest)fc.getExternalContext().getRequest(), 
				(HttpServletResponse)fc.getExternalContext().getResponse());
	}

}
