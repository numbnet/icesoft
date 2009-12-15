package org.icepush.integration.jsp.samples.register;

import org.icepush.PushContext;
import org.icepush.integration.common.notify.GroupNotifier;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.annotation.PreDestroy;

public class IntervalTrigger extends GroupNotifier{
    private long interval;
    private long counter;
    private Timer timer;
    private TimerList timerList;
    private Hashtable offsets;

    public IntervalTrigger() {
	super();
	interval = -1;
        timer = null;
	counter = Long.MIN_VALUE;
	timerList = null;
	offsets = new Hashtable();
    }

    public long getInterval() {
	return interval;
    }
    public void setInterval(long interval) {
	this.interval = interval;
	startTimer();
    }

@Override
    public void setGroup(String grp) {
	super.setGroup(grp);
	if (!offsets.containsKey(getGroup())) {
	    offsets.put(getGroup(), new Long(counter));
	}
    }

    public long getCounter() {
	Long offset = (Long)offsets.get(getGroup());
	if (offset == null) {
	    return 0;
	} else {
	    return counter - offset.longValue();
	}
    }

    private void startTimer() {
	if (timer == null) {
	    timer = new Timer(true);
	    if (timerList != null) {
		System.out.println("Adding timer at startTimer");
		timerList.addTimer(timer);
	    }
	    timer.scheduleAtFixedRate(new TimerTask() {
		    public void run() {
			counter++;
			Enumeration e = offsets.keys();
			while (e.hasMoreElements()) {
			    String grp = (String) e.nextElement();
			    System.out.println(System.currentTimeMillis() + ": Pushing to " + grp);
			    push(grp);
			}
		    }
		}, interval, interval);
	}
    }

    public void setTimerList(TimerList timerList) {
	this.timerList = timerList;
	if (timer != null) {
	    System.out.println("Adding timer at setTimerList");
	    timerList.addTimer(timer);
	}
    }
}
