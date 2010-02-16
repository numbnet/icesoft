package org.icepush.samples.icechat.spring.impl;

import org.icepush.samples.icechat.beans.view.BaseChatManagerViewControllerBean;
import org.icepush.samples.icechat.spring.mvc.PushRequestManager;
import org.icepush.samples.icechat.IPushRequestContext;

public class BaseChatManagerViewController extends BaseChatManagerViewControllerBean {
    private PushRequestManager pushRequestManager;

    public BaseChatManagerViewController() {
        super();
    }

    public PushRequestManager getPushRequestManager() {
        return pushRequestManager;
    }

    public void setPushRequestManager(PushRequestManager pushRequestManager) {
        this.pushRequestManager = pushRequestManager;
    }

    public IPushRequestContext getPushRequestContext() {
        if ((this.pushRequestContext == null) && (pushRequestManager != null)) {
            this.setPushRequestContext(pushRequestManager.getPushRequestContext());
        }

        return this.pushRequestContext;
    }
}
