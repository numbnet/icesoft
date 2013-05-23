/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

package org.icemobile.jsp.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icemobile.jsp.util.MobiJspConstants;
import org.icemobile.util.Utils;


@WebServlet( name="ICEmobileResourceServlet", 
	urlPatterns = {MobiJspConstants.RESOURCE_BASE_URL+"/*"}, 
	loadOnStartup=1)
public class ResourceServlet extends HttpServlet{
	
	private final Date lastModified = new Date();
	private String startupTime; 
	private ClassLoader loader;
	private ServletContext servletContext;
	
	private static final Logger log = Logger.getLogger(ResourceServlet.class.getName());
	
	public void init(final ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        this.loader = this.getClass().getClassLoader();
        this.servletContext = servletConfig.getServletContext();
        this.startupTime = Utils.getHttpDateFormat().format(lastModified);
    }

	
	public void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String modifedHeader = httpServletRequest
                .getHeader("If-Modified-Since");
        if (null != modifedHeader) {
            try {
                Date modifiedSince = Utils.getHttpDateFormat().parse(modifedHeader);
                if (modifiedSince.getTime() + 1000 > lastModified.getTime()) {
                    //respond with a not-modifed
                    httpServletResponse.setStatus(304);
                    httpServletResponse.setDateHeader(
                            "Date", new Date().getTime());
                    httpServletResponse.setDateHeader(
                            "Last-Modified", lastModified.getTime());
                    return;
                }
            } catch (ParseException e) {
                //if the headers are corrupted, still just serve the resource
                log.log(Level.FINE, "failed to parse date: " + modifedHeader, e);
            } catch (NumberFormatException e) {
                //if the headers are corrupted, still just serve the resource
                log.log(Level.FINE, "failed to parse date: " + modifedHeader, e);
            }
        }

        String path = httpServletRequest.getPathInfo();
        String resourceAbsPath = MobiJspConstants.JAR_RESOURCE_PATH + path;
        final InputStream in = loader.getResourceAsStream(resourceAbsPath);
        if (null == in) {
            httpServletResponse.setStatus(404, "Resource not found, :( " + resourceAbsPath + ", path="+path);
            return;
        }
        String mimeType = servletContext.getMimeType(path);
        httpServletResponse.setHeader("Content-Type", mimeType);
        httpServletResponse.setHeader("Last-Modified", startupTime);

        OutputStream out = httpServletResponse.getOutputStream();

        Utils.copyStream(in, out);
    }

}
