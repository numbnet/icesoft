package org.icepush.ws.samples.icepushplace.wsclient;

import java.lang.Exception;

public class BadWorldException extends Exception {
    public BadWorldException(String world) {
	super(world);
    }
    public String getWorld() {
	return this.toString();
    }
}