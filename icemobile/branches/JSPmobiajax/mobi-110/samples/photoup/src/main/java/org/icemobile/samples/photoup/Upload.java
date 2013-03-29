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

package org.icemobile.samples.photoup;

import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import java.io.IOException;
import java.io.File;

@WebServlet(urlPatterns = {"/index.html"})
@MultipartConfig
@WebListener
public class Upload extends HttpServlet implements
        HttpSessionListener {

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/reflect.jsp")
                .forward(request, response);
    }


    public void sessionCreated(HttpSessionEvent event) {
        ServletContext servletContext = event.getSession()
                .getServletContext();
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        ServletContext servletContext  = event.getSession().getServletContext();
        try {
            String dirPath = servletContext.getRealPath("/images") + "/";
            File imageFile = new File(dirPath + 
                    event.getSession().getId() + ".jpg");
            imageFile.delete();
        } catch (Exception e)  {
            servletContext.log("Failed to delete image", e);
        }
    }

}
