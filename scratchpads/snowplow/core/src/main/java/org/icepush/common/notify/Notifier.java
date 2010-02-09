package org.icepush.common.notify;

import org.icepush.PushContext;

public class Notifier {
    private PushContext pushContext;

    public Notifier() {
    }
    
    public PushContext getPushContext() {
	return pushContext;
    }
    public void setPushContext(PushContext pc) {
	pushContext = pc;
    }

    public void push(String group) {
	pushContext.push(group);
    }
}
