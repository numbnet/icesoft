package org.icepush.integration.common.notify;

import org.icepush.PushContext;

public class GroupNotifier extends Notifier {
    private String group;

    public GroupNotifier() {
	super();
	group=null;
    }
    
    public String getGroup() {
	return group;
    }
    public void setGroup(String grp) {
       group = grp;
    }

    public void push() {
	getPushContext().push(group);
    }
}
