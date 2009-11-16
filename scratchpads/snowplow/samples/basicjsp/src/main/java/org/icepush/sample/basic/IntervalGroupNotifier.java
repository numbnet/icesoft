package org.icepush.sample.basic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import org.icepush.PushContext;

import java.util.Timer;
import java.util.TimerTask;

public class IntervalGroupNotifier extends BasicGroupNotifier {
    private long interval;
    private Timer timer;
    private Integer counter=new Integer(0);

    public IntervalGroupNotifier() {
	super();
	interval = -1;
        timer = null;
    }

    public long getInterval() {
	return interval;
    }
    public void setInterval(long interval) {
	this.interval = interval;
	startTimer();
    }
    public Integer getCounter() {
	return counter;
    }

    @Override
    public void run() {
	startTimer();
    }
    
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
	response.setContentType("text/html");
	String body = counter.toString();
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
		    counter++;
		    push();
		}
	    }, interval, interval);
    }
}
