package com.icesoft.faces.webapp.http.portlet;

import com.icesoft.faces.env.RequestAttributes;

import javax.portlet.RenderRequest;
import java.util.Enumeration;

public class PortletRequestAttributes implements RequestAttributes {
    private final RenderRequest request;

    public PortletRequestAttributes(RenderRequest request) {
        this.request = request;
    }

    public Object getAttribute(String name) {
        return request.getAttribute(name);
    }

    public Enumeration getAttributeNames() {
        return request.getAttributeNames();
    }

    public void removeAttribute(String name) {
        request.removeAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        request.setAttribute(name, value);
    }
}
