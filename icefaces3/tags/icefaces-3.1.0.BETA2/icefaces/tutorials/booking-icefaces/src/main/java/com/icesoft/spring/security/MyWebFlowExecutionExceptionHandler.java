/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

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
