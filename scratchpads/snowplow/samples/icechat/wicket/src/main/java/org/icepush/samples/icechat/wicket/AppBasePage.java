package org.icepush.samples.icechat.wicket;

import javax.inject.Inject;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.image.ContextImage;
import org.icepush.samples.icechat.cdi.service.ChatServiceApplicationBean;

public class AppBasePage extends WebPage {

        @Inject
        ChatServiceApplicationBean chatService;

	public AppBasePage() {
		super();
                add(new ContextImage("banner_hdr","./img/banner_hdr.jpg"));
                add(new ContextImage("wicketLogo","./img/logos/wicket.png"));
	}
}

