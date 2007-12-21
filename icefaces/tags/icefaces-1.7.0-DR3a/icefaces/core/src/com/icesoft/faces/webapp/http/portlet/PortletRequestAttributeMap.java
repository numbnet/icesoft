package com.icesoft.faces.webapp.http.portlet;

import com.icesoft.faces.context.AbstractCopyingAttributeMap;

import javax.portlet.PortletRequest;
import java.util.Enumeration;

public class PortletRequestAttributeMap extends AbstractCopyingAttributeMap {
    private PortletRequest request;

    public PortletRequestAttributeMap(PortletRequest request) {
        this.request = request;
        initialize();
    }

    public Enumeration getAttributeNames() {
        return request.getAttributeNames();
    }

    public Object getAttribute(String name) {
        return request.getAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        request.setAttribute(name, value);
    }

    public void removeAttribute(String name) {
        request.removeAttribute(name);
    }
}