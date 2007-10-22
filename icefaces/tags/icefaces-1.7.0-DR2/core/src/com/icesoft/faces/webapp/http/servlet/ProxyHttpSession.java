package com.icesoft.faces.webapp.http.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

public class ProxyHttpSession implements HttpSession {
    protected HttpSession session;

    public ProxyHttpSession(HttpSession session) {
        this.session = session;
    }

    public long getCreationTime() {
        return session.getCreationTime();
    }

    public String getId() {
        return session.getId();
    }

    public long getLastAccessedTime() {
        return getLastAccessedTime();
    }

    public ServletContext getServletContext() {
        return session.getServletContext();
    }

    public void setMaxInactiveInterval(int i) {
        session.setMaxInactiveInterval(i);
    }

    public int getMaxInactiveInterval() {
        return session.getMaxInactiveInterval();
    }

    public HttpSessionContext getSessionContext() {
        return session.getSessionContext();
    }

    public Object getAttribute(String string) {
        return session.getAttribute(string);
    }

    public Object getValue(String string) {
        return session.getValue(string);
    }

    public Enumeration getAttributeNames() {
        return session.getAttributeNames();
    }

    public String[] getValueNames() {
        return session.getValueNames();
    }

    public void setAttribute(String string, Object object) {
        session.setAttribute(string, object);
    }

    public void putValue(String string, Object object) {
        session.putValue(string, object);
    }

    public void removeAttribute(String string) {
        session.removeAttribute(string);
    }

    public void removeValue(String string) {
        session.removeValue(string);
    }

    public void invalidate() {
        session.invalidate();
    }

    public boolean isNew() {
        return session.isNew();
    }
}
