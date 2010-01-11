package org.icepush.samples.icechat.wicket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.icepush.samples.icechat.AbstractPushRequestContext;
import org.icepush.samples.icechat.IPushRequestContext;
import org.icepush.samples.icechat.beans.model.BaseCredentialsBean;
import org.icepush.samples.icechat.wicket.controller.LoginController;

public class AppBasePage extends WebPage {

	LoginController loginController;

	//@EJB
        //private ChatServiceLocal chatService;
	
	IPushRequestContext pushRequestContext;

	CredentialsBean credentialsBean = new CredentialsBean();
	
	class CredentialsBean extends BaseCredentialsBean{}

        CompoundPropertyModel compoundCredentialsBean = new CompoundPropertyModel(credentialsBean);

	public AppBasePage() {
		super();
                add(new ContextImage("banner_hdr","./img/banner_hdr.jpg"));
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

    public CompoundPropertyModel getCompoundCredentialsBean() {
        return compoundCredentialsBean;
    }

    public void setCompoundCredentialsBean(CompoundPropertyModel compoundCredentialsBean) {
        this.compoundCredentialsBean = compoundCredentialsBean;
    }

}

