package org.icepush.samples.icechat.spring.mvc;

import org.icepush.samples.icechat.spring.impl.BasePushRequestContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PushRequestManager {
    protected final Log logger = LogFactory.getLog(getClass());

    private BasePushRequestContext pushRequestContext;

    public PushRequestManager() {
    }

    public BasePushRequestContext getPushRequestContext() {
        return pushRequestContext;
    }

    public void setPushRequestContext(BasePushRequestContext pushRequestContext) {
        if (pushRequestContext != null) {
            logger.info("PushRequestManager init for ID: " + pushRequestContext.getCurrentPushId());
        }

        this.pushRequestContext = pushRequestContext;
    }
}
