package org.icepush.integration.jsp.samples.region;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import org.icepush.PushContext;
import org.icepush.integration.common.notify.BasicGroupNotifier;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Hashtable;
import java.util.Enumeration;

public class IntervalGroupNotifier extends BasicGroupNotifier {
    private long interval;
    private Timer timer;
    private Hashtable counters;

    public IntervalGroupNotifier() {
	super();
	interval = -1;
        timer = null;
	counters = new Hashtable();
    }

    public long getInterval() {
	return interval;
    }
    public void setInterval(long interval) {
	this.interval = interval;
	startTimer();
    }

    @Override
    public void addGroup(String group) {
	super.addGroup(group);
	if (!counters.containsKey(group)) {
	    counters.put(group, new Integer(0));
	}
    }

    @Override
    public void removeGroup(String group) {
	super.removeGroup(group);
	counters.remove(group);
    }

    @Override
    public void run() {
	startTimer();
    }
    
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
	final String group = (String)request.getParameter("group");
	Integer counter = (Integer)counters.get(group);
	String body;
	if (counter == null) {
	    body = new String("");
	} else {
	    body = counter.toString();
	}
	response.setContentType("text/html");
	PrintWriter writer = response.getWriter();
	writer.write(body);
	response.setContentLength(body.length());
    }


    private void startTimer() {
	if (timer == null) {
	    timer = new Timer(true);
	} else {
	    timer.cancel();
	}
        timer.scheduleAtFixedRate(new TimerTask() {
		public void run() {
		    Enumeration e = counters.keys();
		    while (e.hasMoreElements()) {
			String group = (String)e.nextElement();
			int counter = ((Integer)counters.get(group)).intValue();
			counters.put(group, new Integer(++counter));
		    }
		    pushAll();
		}
	    }, interval, interval);
    }
}
