/*
 * ICESOFT COMMERCIAL SOURCE CODE LICENSE V 1.1
 *
 * The contents of this file are subject to the ICEsoft Commercial Source
 * Code License Agreement V1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the
 * License at
 * http://www.icesoft.com/license/commercial-source-v1.1.html
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * Copyright 2009-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 */
package org.icefaces.samples.showcase.util;

import com.uwyn.jhighlight.renderer.XhtmlRendererFactory;
import com.uwyn.jhighlight.tools.FileUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>The SourceCodeLoaderServlet class is responsible for displaying the JSF
 * source code for a particular example. </p>
 *
 * @since 0.3.0
 */      

public class SourceCodeLoaderServlet extends HttpServlet {

    private static final Pattern JSPX_PATTERN =
            Pattern.compile("<!--.*?-->", Pattern.DOTALL);
    private static final Pattern JAVA_PATTERN =
            Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);


    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        // Setting the context type to text/xml provides style
        // attributes for most browsers which should make reading
        // the code easier.
        response.setContentType("text/html");
        
        // Relative path to where the source code is on the server
        String sourcePath = request.getParameter("path");
        
        if (sourcePath != null) {
            InputStream sourceStream = getServletContext().getResourceAsStream(sourcePath);
                    
            if (sourceStream == null) {
                try {
                    // Work around for websphere
                    sourceStream = new FileInputStream(new File(getServletContext().getRealPath(sourcePath)));
                } catch (Exception failedWorkaround) {
                    failedWorkaround.printStackTrace();
                }
            }
            
            if (sourceStream != null) {
                PrintWriter responseStream = null;
                
                try {
                    responseStream = response.getWriter();                    
                    
                    // Pass the source stream to the response stream
                    StringBuffer stringBuffer = new StringBuffer();
                    int ch;
                    while ((ch = sourceStream.read()) != -1) {
                        stringBuffer.append((char) ch);
                    }
                    
                    // Remove the license from the source code
                    Matcher m = JSPX_PATTERN.matcher(stringBuffer);
                    String toReturn = "";
                    if (m.find(0)) {
                        toReturn = m.replaceFirst("// ICESOFT COMMERCIAL SOURCE CODE LICENSE V 1.1  (see http://www.icesoft.com/license/commercial-source-v1.1.html)");
                    } else {
                        m = JAVA_PATTERN.matcher(stringBuffer);
                        toReturn = m.replaceFirst("/* ICESOFT COMMERCIAL SOURCE CODE LICENSE V 1.1  (see http://www.icesoft.com/license/commercial-source-v1.1.html) */\n");
                    }
                    
                    // Check the extension
                    String name = sourcePath.substring(sourcePath.lastIndexOf("/") + 1);
                    String type = "";
                    if (sourcePath.endsWith(".java")) {
                        type = XhtmlRendererFactory.JAVA;
                    } else if (sourcePath.endsWith(".xhtml")) {
                        type = XhtmlRendererFactory.XHTML;
                    }
                    
                    // Highlight properly and print
                    String highlight = XhtmlRendererFactory.getRenderer(type).highlight(name, toReturn, "utf8", false);
                    responseStream.print(highlight);
                } catch (Exception failedWrite) {
                    failedWrite.printStackTrace();
                } finally {
                    // Close the source
                    if (sourceStream != null) {
                        try{
                            sourceStream.close();
                        } catch (Exception ignoredClose) { }
                    }
                    // Close the response
                    if (responseStream != null) {
                        try{
                            responseStream.close();
                        } catch (Exception ignoredClose) { }
                    }
                }
            }
        }
    }

}
