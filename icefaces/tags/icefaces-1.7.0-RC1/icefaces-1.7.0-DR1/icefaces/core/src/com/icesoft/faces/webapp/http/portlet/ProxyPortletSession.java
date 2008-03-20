package com.icesoft.faces.webapp.http.portlet;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;
import java.util.Enumeration;

public class ProxyPortletSession implements PortletSession {
    private PortletSession session;

    public ProxyPortletSession(PortletSession session) {
        this.session = session;
    }

    public Object getAttribute(String string) {
        return session.getAttribute(string);
    }

    public Object getAttribute(String string, int i) {
        return session.getAttribute(string, i);
    }

    public Enumeration getAttributeNames() {
        return session.getAttributeNames();
    }

    public Enumeration getAttributeNames(int i) {
        return session.getAttributeNames();
    }

    public long getCreationTime() {
        return session.getCreationTime();
    }

    public String getId() {
        return session.getId();
    }

    public long getLastAccessedTime() {
        return session.getLastAccessedTime();
    }

    public int getMaxInactiveInterval() {
        return session.getMaxInactiveInterval();
    }

    public void invalidate() {
        session.invalidate();
    }

    public boolean isNew() {
        return session.isNew();
    }

    public void removeAttribute(String string) {
        session.removeAttribute(string);
    }

    public void removeAttribute(String string, int i) {
        session.removeAttribute(string, i);
    }

    public void setAttribute(String string, Object object) {
        session.setAttribute(string, object);
    }

    public void setAttribute(String string, Object object, int i) {
        session.setAttribute(string, object, i);
    }

    public void setMaxInactiveInterval(int i) {
        session.setMaxInactiveInterval(i);
    }

    public PortletContext getPortletContext() {
        return session.getPortletContext();
    }
}
