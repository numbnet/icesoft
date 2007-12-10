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
import com.icesoft.faces.webapp.http.servlet.ServletEnvironmentRequest;

public class SwfLifecycleExecutor extends LifecycleExecutor  {

    public void apply(FacesContext facesContext)  {
        FlowExecutor flowExecutor = (FlowExecutor) 
                SpringWebFlowInstantiationServlet.getFlowExecutor();
        ExternalContext externalContext = facesContext.getExternalContext();
        ServletContext servletContext = (ServletContext) externalContext.getContext();
        HttpServletRequest servletRequest = (HttpServletRequest) externalContext.getRequest();
        ServletResponse servletResponse = (ServletResponse) externalContext.getResponse();
        String flowExecutionKey = servletRequest.getParameter("org.springframework.webflow.FlowExecutionKey");
        if (null != flowExecutionKey)  {
            String path1 = servletRequest.getPathInfo();
            path1 = firstSegment(path1);
//Fully correcting the reqeustPathInfo mysteriously causes strange behavior
//perhaps the flowId is sometimes invalid
//            if (!path1.equals("executions")) {
                ((ServletEnvironmentRequest) servletRequest).setPathInfo("/executions/" + path1 + "/" + flowExecutionKey);
//            }
        }
        try {
            ServletExternalContext servletExternalContext = 
            new ServletExternalContext(
                    servletContext, servletRequest, servletResponse );
            servletExternalContext.executeFlowRequest(flowExecutor);
        } catch (IOException e)  {
            throw new FacesException(e);
        }
    }

    public String firstSegment(String path)  {
        String path1 = path.substring(1);
        int end = path1.indexOf("/");
        if (-1 == end) {
            end = path1.length();
        }
        path1 = path1.substring(0, end);
        return path1;
    }
}
