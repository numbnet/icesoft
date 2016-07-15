/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icemobile.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;
import java.util.logging.Logger;

@MultipartConfig
public class MultipartFacesServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(MultipartFacesServlet.class.getName());

    public void init()  {
        log.info("Fix for Multipart Faces Servlet active.");
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext servletContext = request.getServletContext();
        RequestDispatcher facesDispatcher = servletContext
                .getNamedDispatcher("Faces Servlet");
        if( null == facesDispatcher ){
            throw new ServletException("Missing Faces Servlet. Please configure the Faces " +
                    "Servlet in the web.xml for use with the ICEmobile " +
                    "MultipartFacesServlet.");
        }
        else{
            facesDispatcher.forward(request, response);
        }
    }

}
