package org.icepush.sample.basic;

import org.icepush.PushContext;

public class BasicGroupNotifier {
    private PushContext pushContext;
    private String group;

    public BasicGroupNotifier() {
	pushContext = null;
	group = null;
    }
    public String getGroup() {
	return group;
    }
    public void setGroup(String grp) {
	this.group = grp;
    }
    public PushContext getPushContext() {
	return pushContext;
    }
    public void setPushContext(PushContext pc) {
	this.pushContext = pc;
    }
    public void push() {
	if (pushContext != null && group != null) {
	    pushContext.push(group);
	}     
    }
}
