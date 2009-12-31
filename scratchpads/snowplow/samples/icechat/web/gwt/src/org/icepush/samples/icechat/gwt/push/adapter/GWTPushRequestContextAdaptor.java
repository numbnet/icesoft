package org.icepush.samples.icechat.gwt.push.adapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;


public class GWTPushRequestContextAdaptor extends AbstractPushRequestContext{
	
	private GWTPushRequestContextAdaptor(ServletContext context, HttpServletRequest req, HttpServletResponse resp){
		this.initialize(context,req, resp);
	}

	public void initialize(ServletContext context, HttpServletRequest req, HttpServletResponse resp){
		this.intializePushContext(context,req,resp);
		req.getSession().setAttribute("pushContext", this);
	}
	
	public static GWTPushRequestContextAdaptor getInstance(ServletContext context, HttpServletRequest request, HttpServletResponse response){
	
		GWTPushRequestContextAdaptor instance = (GWTPushRequestContextAdaptor)request.getSession().getAttribute("pushContext");
		
		if(instance == null){
			instance = new GWTPushRequestContextAdaptor(context, request, response);
		}
		
		return instance;
	}
	
}