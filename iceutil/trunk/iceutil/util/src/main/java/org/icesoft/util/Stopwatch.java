package org.icesoft.util;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Stopwatch
implements Serializable {
    private static final long serialVersionUID = 2804117660842718554L;

    private static final Logger LOGGER = Logger.getLogger(Stopwatch.class.getName());

    private boolean running;
    private long startTimestamp;
    private long stopTimestamp;

    public Stopwatch() {
        // Do nothing.
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public long getStopTimestamp() {
        return stopTimestamp;
    }

    public long getTimeInMilli() {
        return getTimeInNano() / 1000000;
    }

    public long getTimeInNano() {
        if (!isRunning()) {
            long _startTimestamp = getStartTimestamp();
            long _stopTimestamp = getStopTimestamp();
            if (_startTimestamp != 0L && _stopTimestamp != 0L) {
                return _stopTimestamp - _startTimestamp;
            } else {
                return 0L;
            }
        } else {
            return System.nanoTime() - getStartTimestamp();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void reset() {
        setStartTimestamp(0L);
        setStopTimestamp(0L);
    }

    public void start() {
        if (!isRunning()) {
            setRunning(true);
            setStartTimestamp(System.nanoTime());
        }
    }

    public void stop() {
        if (isRunning()) {
            setStopTimestamp(System.nanoTime());
            setRunning(false);
        }
    }

    protected void setRunning(final boolean running) {
        this.running = running;
    }

    protected void setStartTimestamp(final long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    protected void setStopTimestamp(final long stopTimestamp) {
        this.stopTimestamp = stopTimestamp;
    }
}