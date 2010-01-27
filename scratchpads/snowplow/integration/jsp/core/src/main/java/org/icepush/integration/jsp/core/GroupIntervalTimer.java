package org.icepush.integration.jsp.core;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Enumeration;
import java.util.Hashtable;
import org.icepush.PushContext;

public class GroupIntervalTimer implements ServletContextListener {

    private Timer timer;
    private Hashtable timerTasks;
    private PushContext pushContext;

    public GroupIntervalTimer() {
    }

    public void contextInitialized(ServletContextEvent e) {
	timer = new Timer(true);
	timerTasks = new Hashtable();
	e.getServletContext().setAttribute("ICEpushJSPtimer", this);
    }

    public void contextDestroyed(ServletContextEvent e) {
	timer.cancel();
    }

    public void addGroup(String group, long interval) throws IllegalStateException {
	GroupTimerTask timerTask = (GroupTimerTask)timerTasks.get(group);
	if (timerTask == null) {
	    if (interval > 0) {
		// New group;
		startTimerTask(group, interval);
	    }
	} else {
	    // Existing group;
	    if (interval > 0) {
		if (interval != timerTask.getInterval()) {
		    // Start with new interval;
		    timerTask.cancel();
		    startTimerTask(group, interval);
		} else {
		    // Same interval so do nothing;
		}
	    } else {
		// Interval zero, so get rid of it;
		timerTasks.remove(group);
	    }
	}
    }

    private void startTimerTask(String group, long interval) throws IllegalStateException {
	GroupTimerTask timerTask = new GroupTimerTask(group, interval);
	timerTasks.put(group,timerTask);
	try {
	    timer.scheduleAtFixedRate(timerTask,interval,interval);
	} catch (Exception e) {
	    throw new IllegalStateException("GroupIntervalTimer could not start timerTask for group: " + group);
	}
    }

    public void setPushContext(PushContext pc) {
	pushContext = pc;
    }
    public PushContext getPushContext() {
	return pushContext;
    }

    private class GroupTimerTask extends TimerTask {
	private long interval;
	private String group;

	public GroupTimerTask(String group, long interval) {
	    super();
	    this.interval = interval;
	    this.group = group;
	}

	public long getInterval() {
	    return interval;
	}
	public void setInterval(long interval) {
	    this.interval = interval;
	}

	public void run() {
	    pushContext.push(group);
	}
    }
}
