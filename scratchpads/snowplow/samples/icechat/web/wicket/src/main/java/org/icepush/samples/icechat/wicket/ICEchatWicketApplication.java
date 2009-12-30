package org.icepush.samples.icechat.wicket;

import org.apache.wicket.Page;
import org.jboss.weld.wicket.WeldApplication;


public class ICEchatWicketApplication  extends WeldApplication{
	
	@Override
	public Class<? extends Page> getHomePage()
	{
		return HomePage.class;
	}

}
