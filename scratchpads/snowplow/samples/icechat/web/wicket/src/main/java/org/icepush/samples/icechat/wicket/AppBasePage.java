package org.icepush.samples.icechat.wicket;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.icepush.samples.icechat.AbstractPushRequestContext;
import org.icepush.samples.icechat.IPushRequestContext;
import org.icepush.samples.icechat.cdi.service.ChatServiceApplicationBean;

public class AppBasePage extends WebPage {

        @Inject
        ChatServiceApplicationBean chatService;

	//@EJB
        //private ChatServiceLocal chatService;
	
	IPushRequestContext pushRequestContext;

	public AppBasePage() {
		super();
                add(new ContextImage("banner_hdr","./img/banner_hdr.jpg"));
                add(new ContextImage("wicketLogo","./img/logos/wicket.png"));
	}
	
	class WicketPushRequestContextAdapter extends AbstractPushRequestContext{
		public WicketPushRequestContextAdapter(){
			//TODO tied to servlet, not portlet
			WebRequest webRequest = (WebRequest)getWebRequestCycle().getRequest();
			WebResponse webResponse = (WebResponse)getWebRequestCycle().getResponse();
			
			intializePushContext((HttpServletRequest)webRequest.getHttpServletRequest(), 
					(HttpServletResponse)webResponse.getHttpServletResponse());
		}
	}
	
	public IPushRequestContext getPushRequestContext(){
		if( pushRequestContext == null ){
			pushRequestContext = new WicketPushRequestContextAdapter();
		}
		return pushRequestContext;
	}
}

