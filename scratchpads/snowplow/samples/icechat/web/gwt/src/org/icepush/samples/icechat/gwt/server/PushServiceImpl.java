package org.icepush.samples.icechat.gwt.server;

import org.icepush.samples.icechat.gwt.client.PushService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icepush.samples.icechat.gwt.push.adapter.GWTPushRequestContextAdaptor;


public class PushServiceImpl extends RemoteServiceServlet implements PushService{

	public String getPushId(){
		HttpServletRequest req = getThreadLocalRequest();
		HttpServletResponse resp = getThreadLocalResponse();
		
		GWTPushRequestContextAdaptor adaptor = GWTPushRequestContextAdaptor.getInstance(this.getServletContext(), req,resp);
		adaptor.getPushContext().addGroupMember("chatRoom1",adaptor.getCurrentPushId());
		return adaptor.getCurrentPushId();
	}

}