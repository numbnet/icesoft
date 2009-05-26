/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icefaces.push.servlet;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.icefaces.push.http.ResponseHandler;
import org.icefaces.push.http.Server;

import javax.servlet.ServletContext;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class AsyncAdaptingServlet extends ThreadBlockingAdaptingServlet {
    private static Logger log = Logger.getLogger("org.icefaces.pushservlet");
    private final Server server;

    public AsyncAdaptingServlet(final Server server)  {
        super(server);
        this.server = server;
System.out.println("Using Servlet 3.0 AsyncContext");
    }

    public void service(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        String pathInfo = request.getPathInfo();
        if (!"/send-updated-views".equals(pathInfo))  {
            super.service(request, response);
            return;
        }
        if (!request.isAsyncSupported())  {
System.out.println("ThreadBlocking delegation");
            super.service(request, response);
            return;
        }
        AsyncRequestResponse requestResponse = new AsyncRequestResponse(request, response);
        server.service(requestResponse);
    }

    public void shutdown() {
        server.shutdown();
    }

    private class AsyncRequestResponse extends ServletRequestResponse  {
        AsyncContext asyncContext;

        public AsyncRequestResponse(final HttpServletRequest request, 
                final HttpServletResponse response) throws Exception {
            super(request, response);
            if (!request.isAsyncStarted())  {
                asyncContext = request.startAsync();
System.out.println(Integer.toHexString(this.hashCode()) + " startAsync on " + asyncContext);
            }
            asyncContext = request.getAsyncContext();
System.out.println(Integer.toHexString(this.hashCode()) + " get asyncContext for " + asyncContext);
        }

        public void respondWith(final ResponseHandler handler) 
                throws Exception {
            super.respondWith(handler);
System.out.println(Integer.toHexString(this.hashCode()) + " resuming " + asyncContext);
            asyncContext.getResponse().flushBuffer();
            asyncContext.complete();
        }

    }
}
