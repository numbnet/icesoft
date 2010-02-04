package org.icepush.integration.wicket.samples.pushpanel;

public class HomePage extends BasePage {

    public HomePage() {
        add(new LeftPushPanel("leftPushPanel"));
        add(new PushPanel("pushPanel"));
        add(new RightPushPanel("rightPushPanel"));
    }
}
