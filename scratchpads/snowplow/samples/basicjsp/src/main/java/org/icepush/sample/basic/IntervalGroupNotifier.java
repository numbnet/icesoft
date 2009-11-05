package org.icepush.sample.basic;

import org.icepush.PushContext;

import java.util.Timer;
import java.util.TimerTask;

public class IntervalGroupNotifier extends BasicGroupNotifier {
    private long interval;
    private Timer timer;

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

    private void startTimer() {
	if (timer == null) {
	    timer = new Timer(true);
	} else {
	    timer.cancel();
	}
        timer.scheduleAtFixedRate(new TimerTask() {
		public void run() {
		    push();
		}
	    }, 0, interval);
    }
}
