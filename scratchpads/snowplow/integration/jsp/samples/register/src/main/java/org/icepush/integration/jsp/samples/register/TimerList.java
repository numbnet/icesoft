package org.icepush.integration.jsp.samples.register;

import java.util.Timer;
import java.util.Vector;
import java.util.Enumeration;

public class TimerList {
    private Vector timers;

    public TimerList() {
	timers = new Vector();
    }

    public void addTimer(Timer timer) {
	if (!timers.contains(timer)) {
	    timers.add((Object)timer);
	}
    }
    public void stopTimers() {
	for(Enumeration e = timers.elements(); e.hasMoreElements();) {
	    Timer timer = (Timer)e.nextElement();
	    timer.cancel();
	}
    }
}
