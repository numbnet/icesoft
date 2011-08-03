/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icepush.samples.pushtotalk;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class Dispatch implements Filter {
    FilterConfig filterConfig;
    ServletContext servletContext;
    String contextPath;
    private static final String IMAGES = "images/";
    private static final String UPLOAD = "upload/";
    public static String TOPIC = "brogcast.topic";
    private static final String R = "r";

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.servletContext = filterConfig.getServletContext();
        contextPath = servletContext.getContextPath();
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();

        //do not interfere with icepush requests
        if (uri.endsWith(".icepush")) {
            chain.doFilter(request, response);
            return;
        }

        String localURI = uri.substring(contextPath.length() + 1);

        if ((null == localURI) || (0 == localURI.length())) {
            servletContext.getRequestDispatcher("/WEB-INF/default.jsp")
                    .forward(request, response);
            return;
        }

        if (localURI.startsWith(IMAGES)) {
            chain.doFilter(request, response);
            return;
        }

        //generic URL containing topic
        String topic;
        if (localURI.startsWith(UPLOAD)) {
            topic = localURI.substring(UPLOAD.length());
        } else {
            topic = localURI;
        }
        request.setAttribute(TOPIC, topic);

        String updateMarker = request.getParameter(R);
        if (null != updateMarker) {
            servletContext.getRequestDispatcher("/WEB-INF/update.jsp")
                    .forward(request, response);
            return;
        }

        if (localURI.startsWith(UPLOAD)) {
            chain.doFilter(request, response);
            return;
        }

        servletContext.getRequestDispatcher("/WEB-INF/talk.jsp")
                .forward(request, response);

//        chain.doFilter(request, response);

    }

}