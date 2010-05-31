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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package com.icesoft.faces.webapp;

import org.icefaces.push.Configuration;
import org.icefaces.push.http.MimeTypeMatcher;
import org.icefaces.push.servlet.BasicAdaptingServlet;
import org.icefaces.push.servlet.PseudoServlet;
import org.icefaces.push.servlet.ServletContextConfiguration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class ResourceServlet extends HttpServlet {
    private static final CurrentContextPath currentContextPath = new CurrentContextPath();
    private PseudoServlet main;

    public void init(final ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        final ServletContext context = servletConfig.getServletContext();
        final Configuration configuration = new ServletContextConfiguration("org.icefaces", context);
        final MimeTypeMatcher mimeTypeMatcher = new MimeTypeMatcher() {
            public String mimeTypeFor(String extension) {
                return context.getMimeType(extension);
            }
        };
        final FileLocator localFileLocator = new FileLocator() {
            public File locate(String path) {
                URI contextURI = URI.create(currentContextPath.lookup());
                URI pathURI = URI.create(path);
                String result = contextURI.relativize(pathURI).getPath();
                String fileLocation = context.getRealPath(result);
                return new File(fileLocation);
            }
        };
        main = new BasicAdaptingServlet(new ResourceServer(configuration, mimeTypeMatcher, localFileLocator));
    }

    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try {
            currentContextPath.attach(httpServletRequest.getContextPath());
            main.service(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            currentContextPath.detach();
        }
    }

    public void destroy() {
        main.shutdown();
    }

    private static class CurrentContextPath extends ThreadLocal {
        public String lookup() {
            return (String) get();
        }

        public void attach(String path) {
            set(path);
        }

        public void detach() {
            set(null);
        }
    }
}
