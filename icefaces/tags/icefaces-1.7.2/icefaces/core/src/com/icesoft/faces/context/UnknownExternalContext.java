package com.icesoft.faces.context;

import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.http.common.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class UnknownExternalContext extends BridgeExternalContext {
    private static final String message = "Unknown environment.";

    public UnknownExternalContext(CommandQueue commandQueue, Configuration configuration) {
        super("unknown view id", commandQueue, configuration);
    }

    public void dispatch(String path) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    public String encodeActionURL(String url) {
        throw new UnsupportedOperationException(message);
    }

    public String encodeNamespace(String name) {
        throw new UnsupportedOperationException(message);
    }

    public String encodeResourceURL(String url) {
        throw new UnsupportedOperationException(message);
    }

    public Map getApplicationMap() {
        throw new UnsupportedOperationException(message);
    }

    public String getAuthType() {
        throw new UnsupportedOperationException(message);
    }

    public Object getContext() {
        throw new UnsupportedOperationException(message);
    }

    public String getInitParameter(String name) {
        throw new UnsupportedOperationException(message);
    }

    public Map getInitParameterMap() {
        throw new UnsupportedOperationException(message);
    }

    public String getRemoteUser() {
        throw new UnsupportedOperationException(message);
    }

    public Object getRequest() {
        throw new UnsupportedOperationException(message);
    }

    public String getRequestContextPath() {
        throw new UnsupportedOperationException(message);
    }

    public Map getRequestCookieMap() {
        throw new UnsupportedOperationException(message);
    }

    public Map getRequestHeaderMap() {
        throw new UnsupportedOperationException(message);
    }

    public Map getRequestHeaderValuesMap() {
        throw new UnsupportedOperationException(message);
    }

    public Locale getRequestLocale() {
        throw new UnsupportedOperationException(message);
    }

    public Iterator getRequestLocales() {
        throw new UnsupportedOperationException(message);
    }

    public Map getRequestMap() {
        throw new UnsupportedOperationException(message);
    }

    public Map getRequestParameterMap() {
        throw new UnsupportedOperationException(message);
    }

    public Iterator getRequestParameterNames() {
        throw new UnsupportedOperationException(message);
    }

    public Map getRequestParameterValuesMap() {
        throw new UnsupportedOperationException(message);
    }

    public String getRequestPathInfo() {
        throw new UnsupportedOperationException(message);
    }

    public String getRequestServletPath() {
        throw new UnsupportedOperationException(message);
    }

    public URL getResource(String path) throws MalformedURLException {
        throw new UnsupportedOperationException(message);
    }

    public InputStream getResourceAsStream(String path) {
        throw new UnsupportedOperationException(message);
    }

    public Set getResourcePaths(String path) {
        throw new UnsupportedOperationException(message);
    }

    public Object getResponse() {
        throw new UnsupportedOperationException(message);
    }

    public Object getSession(boolean create) {
        throw new UnsupportedOperationException(message);
    }

    public Map getSessionMap() {
        throw new UnsupportedOperationException(message);
    }

    public Principal getUserPrincipal() {
        throw new UnsupportedOperationException(message);
    }

    public boolean isUserInRole(String role) {
        throw new UnsupportedOperationException(message);
    }

    public void log(String message) {
        throw new UnsupportedOperationException(message);
    }

    public void log(String message, Throwable throwable) {
        throw new UnsupportedOperationException(message);
    }

    public void redirect(String url) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    public String getRequestURI() {
        throw new UnsupportedOperationException(message);
    }

    public Writer getWriter(String encoding) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    public void switchToNormalMode() {
        throw new UnsupportedOperationException(message);
    }

    public void switchToPushMode() {
        throw new UnsupportedOperationException(message);
    }

    public void update(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException(message);
    }

    public void updateOnPageLoad(Object request, Object response) {
        throw new UnsupportedOperationException(message);
    }

	public void removeSeamAttributes() {
		 throw new UnsupportedOperationException(message);
	}
}
