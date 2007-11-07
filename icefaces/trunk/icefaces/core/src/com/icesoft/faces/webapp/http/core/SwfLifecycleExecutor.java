package com.icesoft.faces.webapp.http.core;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletResponse;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.context.servlet.ServletExternalContext;

import com.icesoft.faces.webapp.http.servlet.SpringWebFlowInstantiationServlet;

public class SwfLifecycleExecutor extends LifecycleExecutor  {

    public void apply(FacesContext facesContext)  {
        FlowExecutor flowExecutor = (FlowExecutor) 
                SpringWebFlowInstantiationServlet.getFlowExecutor();
        System.out.println("PageServer: FlowExecutor is  " + flowExecutor);
        System.out.println("PageServer: FacesContext is  " + facesContext);
        ExternalContext externalContext = facesContext.getExternalContext();
        ServletContext servletContext = (ServletContext) externalContext.getContext();
        HttpServletRequest servletRequest = (HttpServletRequest) externalContext.getRequest();
        ServletResponse servletResponse = (ServletResponse) externalContext.getResponse();
        //((ServletEnvironmentRequest) servletRequest).setPathInfo(servletRequest.getParameter("rpi"));
        System.out.println("Request is " + servletRequest);
        System.out.println("Executing Flow Request " + servletRequest.getPathInfo());
        try {
            new ServletExternalContext(
                    servletContext, servletRequest, servletResponse )
                    .executeFlowRequest(flowExecutor);
        } catch (IOException e)  {
            throw new FacesException(e);
        }
    }

}