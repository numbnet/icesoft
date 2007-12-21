package com.icesoft.faces.webapp.http.servlet;

import javax.servlet.http.HttpSession;

public class InterceptingServletSession extends ProxyHttpSession {
    private final SessionDispatcher.Monitor sessionMonitor;

    public InterceptingServletSession(HttpSession session, SessionDispatcher.Monitor sessionMonitor) {
        super(session);
        this.sessionMonitor = sessionMonitor;
    }

    public void invalidate() {
        sessionMonitor.shutdown();
    }
}
