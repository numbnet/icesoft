package com.icesoft.faces.webapp.http.core;

import java.io.IOException;

import java.util.Map;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletResponse;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.executor.FlowExecutionResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;

import com.icesoft.faces.webapp.http.servlet.SpringWebFlowInstantiationServlet;
import com.icesoft.faces.webapp.http.servlet.ServletEnvironmentRequest;

public class SwfLifecycleExecutor extends LifecycleExecutor  {

    public void apply(FacesContext facesContext)  {
        FlowExecutor flowExecutor = (FlowExecutor) 
                SpringWebFlowInstantiationServlet.getFlowExecutor();
        ExternalContext externalContext = facesContext.getExternalContext();

        ServletContext servletContext = (ServletContext) externalContext.getContext();
        HttpServletRequest servletRequest = (HttpServletRequest) externalContext.getRequest();
        HttpServletResponse servletResponse = (HttpServletResponse) externalContext.getResponse();
        String flowExecutionKey = servletRequest.getParameter("org.springframework.webflow.FlowExecutionKey");
        String flowId = firstSegment(servletRequest.getPathInfo());
        ServletExternalContext servletExternalContext = 
            new ServletExternalContext(
                    servletContext, servletRequest, servletResponse );
        if (null != flowExecutionKey)  {
            FlowExecutionResult result = flowExecutor.resumeExecution(
                    flowExecutionKey, servletExternalContext);
        } else {
            MutableAttributeMap input = 
                defaultFlowExecutionInputMap(servletRequest);
            FlowExecutionResult result = flowExecutor.launchExecution(
                    flowId, input, servletExternalContext);
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
    
    protected MutableAttributeMap defaultFlowExecutionInputMap(HttpServletRequest request) {
        LocalAttributeMap inputMap = new LocalAttributeMap();
        Map parameterMap = request.getParameterMap();
        Iterator it = parameterMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String name = (String) entry.getKey();
            String[] values = (String[]) entry.getValue();
            if (values.length == 1) {
                inputMap.put(name, values[0]);
            } else {
                inputMap.put(name, values);
            }
        }
        return inputMap;
    }

}
