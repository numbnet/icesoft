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

package org.icemobile.spring.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icemobile.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

public class RangingResourceHttpRequestHandler extends ResourceHttpRequestHandler {
    private static final Log LOG = LogFactory
            .getLog(ResourceHttpRequestHandler.class);
    private static String RANGE = "Range";
    private static String BYTES_PREFIX = "bytes=";
    private static String CONTENT_RANGE = "Content-Range";

    @Autowired
    protected WebApplicationContext context;

    @PostConstruct
    public void init()  {
        List<Resource> locations = new ArrayList<Resource>(1);
        locations.add(context.getResource("/resources/"));
        setLocations(locations);
    }
    
    public void handleRequest(HttpServletRequest request, 
        HttpServletResponse response) throws ServletException, IOException  {
        
        boolean useRanges = false;
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String requestPath = requestURI.substring(contextPath.length());

        
        int rangeStart = 0;
        int rangeEnd = 0; 

        String rangeHeader = request.getHeader(RANGE);
        if (null != rangeHeader)  {
            try {
                if (rangeHeader.startsWith(BYTES_PREFIX))  {
                    String range = rangeHeader
                            .substring(BYTES_PREFIX.length() );
                    int splitIndex = range.indexOf("-");
                    String startString = range.substring(0, splitIndex);
                    String endString = range.substring(splitIndex + 1);
                    rangeStart = Integer.parseInt(startString);
                    // ICE-9256 rangeEnd == 0 means use contentLength below
                    if (!"".equals( endString )) {
                        rangeEnd = Integer.parseInt(endString);
                    }
                    useRanges = true;
                }
            } catch (Exception e)  {
                useRanges = false;
                LOG.warn("Unable to decode range header " + rangeHeader);
            }
        }
        
        String contentType = context.getServletContext()
                .getMimeType(requestPath);
        if( contentType != null ){
            response.setContentType( contentType );
        }
        response.setHeader("Accept-Ranges", "bytes");
        Object[] inputStreamAndContentLength = 
                getInputStreamAndContentLength(requestPath);
        if( inputStreamAndContentLength != null ){
            if (null == contentType)  {
                if (null != inputStreamAndContentLength[2])  {
                    response.setContentType((String)
                            inputStreamAndContentLength[2]);
                }
            }
            if( useRanges ){
                int cl = ((Long)inputStreamAndContentLength[1]).intValue();
                rangeEnd = (rangeEnd == 0) ? cl-1: rangeEnd;
                response.setHeader(CONTENT_RANGE, 
                        "bytes " + rangeStart + "-" + rangeEnd + "/" + cl);
                response.setContentLength(1 + rangeEnd - rangeStart);
                IOUtils.copyStream((InputStream)inputStreamAndContentLength[0], 
                        response.getOutputStream(), rangeStart, rangeEnd);
            } else {
                IOUtils.copyStream((InputStream)inputStreamAndContentLength[0], 
                        response.getOutputStream());
            }
        }
        else{
            response.setStatus(404);
            response.sendError(404);
        }
        
    }
    
    protected Object[] getInputStreamAndContentLength(String requestPath)
        throws IOException{
        
        Resource theResource = context.getResource(requestPath);
        return new Object[]{
                theResource.getInputStream(),
                Long.valueOf(theResource.contentLength()),
                null
            };
    }



}

