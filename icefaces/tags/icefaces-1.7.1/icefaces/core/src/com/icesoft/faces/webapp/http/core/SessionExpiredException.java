package com.icesoft.faces.webapp.http.core;

public class SessionExpiredException extends RuntimeException {
    public SessionExpiredException() {
        super("Session has been expired.");
    }

    public SessionExpiredException(Throwable throwable) {
        super("Session has been expired.", throwable);
    }
}
