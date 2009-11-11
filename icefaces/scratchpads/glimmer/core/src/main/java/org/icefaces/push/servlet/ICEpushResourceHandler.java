package org.icefaces.push.servlet;

import org.icepush.servlet.MainServlet;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

public class ICEpushResourceHandler extends ResourceHandler {
    private static final Pattern ICEpushRequestPattern = Pattern.compile(".*\\.icepush\\.jsf$");
    private ResourceHandler handler;
    private MainServlet mainServlet;

    public ICEpushResourceHandler(ResourceHandler handler) {
        this.handler = handler;
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        mainServlet = new MainServlet(context);
        context.setAttribute(ICEpushResourceHandler.class.getName(), this);
    }

    public Resource createResource(String s) {
        return handler.createResource(s);
    }

    public Resource createResource(String s, String s1) {
        return handler.createResource(s, s1);
    }

    public Resource createResource(String s, String s1, String s2) {
        return handler.createResource(s, s1, s2);
    }

    public boolean libraryExists(String s) {
        return handler.libraryExists(s);
    }

    public void handleResourceRequest(FacesContext facesContext) throws IOException {
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        String requestURI = request.getRequestURI();
        if (ICEpushRequestPattern.matcher(requestURI).find()) {
            try {
                mainServlet.service(request, response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            handler.handleResourceRequest(facesContext);
        }
    }

    public boolean isResourceRequest(FacesContext facesContext) {
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletRequest servletRequest = (HttpServletRequest) externalContext.getRequest();
        String requestURI = servletRequest.getRequestURI();
        return handler.isResourceRequest(facesContext) || ICEpushRequestPattern.matcher(requestURI).find();
    }

    public String getRendererTypeForResourceName(String s) {
        return handler.getRendererTypeForResourceName(s);
    }

    public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            mainServlet.service(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public static void notifyContextShutdown(ServletContext context) {
        ((ICEpushResourceHandler) context.getAttribute(ICEpushResourceHandler.class.getName())).mainServlet.shutdown();
    }
}
