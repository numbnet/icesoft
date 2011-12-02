package com.icesoft.spring.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.webflow.engine.FlowExecutionExceptionHandler;
import org.springframework.webflow.engine.RequestControlContext;
import org.springframework.webflow.execution.FlowExecutionException;

/**
 * This class represents...
 *
 * @author Ben Simpson <ben.simpson@icesoft.com>
 *         Date: 2/28/11
 *         Time: 6:41 PM
 */
public class MyWebFlowExecutionExceptionHandler implements FlowExecutionExceptionHandler {
    public boolean canHandle(FlowExecutionException exception) {
        return exception.getCause() instanceof AccessDeniedException;
    }

    public void handle(FlowExecutionException exception, RequestControlContext context) {
        System.out.println("exception = " + exception);
    }
}
