package org.icepush.integration.jsp.samples.region;

import org.icepush.integration.common.notify.GroupNotifier;

import java.util.Hashtable;

public class GroupNotificationCounter extends GroupNotifier{
    private Hashtable counters;
    private long interval;

    public GroupNotificationCounter() {
	super();
	counters = new Hashtable();
    }

@Override
    public void setGroup(String grp) {
	super.setGroup(grp);
	if (!counters.containsKey(grp)) {
	    counters.put(grp, new Long(System.currentTimeMillis()));
	}
    }

    public long getInterval() {
	return interval;
    }
    public void setInterval(long interval) {
	this.interval = interval;
    }

    public long getCounter() {
	return getCounter(getGroup());
    }

    public long getCounter(String group) {
	Long start = (Long)counters.get(group);
	if (start == null) {
	    return 0;
	} else {
	    long now = System.currentTimeMillis();
	    return (now - start.longValue())/interval;
	}
    }
}
