package org.icepush.samples.icechat;

import org.apache.wicket.Page;
import org.apache.wicket.examples.helloworld.HelloWorld;
import org.apache.wicket.protocol.http.WebApplication;


public class ICEchatWicketApplication  extends WebApplication{
	
	@Override
	public Class<? extends Page> getHomePage()
	{
		return HelloWorld.class;
	}

}
