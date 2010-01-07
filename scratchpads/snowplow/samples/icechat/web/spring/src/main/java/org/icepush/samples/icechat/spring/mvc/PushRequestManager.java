package org.icepush.samples.icechat.spring.mvc;

import org.icepush.samples.icechat.spring.impl.BasePushRequestContext;

public class PushRequestManager {
    private static PushRequestManager singleton = null;
    
    private BasePushRequestContext pushRequestContext;

    private PushRequestManager() {
    }

    public static synchronized PushRequestManager getInstance() {
        if (singleton == null) {
            singleton = new PushRequestManager();
        }

        return (singleton);
    }

    public BasePushRequestContext getPushRequestContext() {
        return pushRequestContext;
    }

    public void setPushRequestContext(BasePushRequestContext pushRequestContext) {
        this.pushRequestContext = pushRequestContext;
    }
}
