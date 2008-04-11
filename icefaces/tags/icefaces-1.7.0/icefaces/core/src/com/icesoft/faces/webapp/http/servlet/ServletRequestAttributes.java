package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.env.RequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class ServletRequestAttributes implements RequestAttributes {
    private final HttpServletRequest request;

    public ServletRequestAttributes(HttpServletRequest request) {
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
