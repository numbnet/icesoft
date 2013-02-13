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
package org.icemobile.spring.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icemobile.application.FileResource;
import org.icemobile.application.Resource;

public class ICEmobileSimulatorResourceUploadInterceptor 
    extends ICEmobileResourceUploadInterceptor{
    
    private static final String SIMULATOR_RESOURCE_KEY = "sim-";
    
    public boolean preHandle(HttpServletRequest request, 
            HttpServletResponse response, Object handler)
        throws Exception {
        
        System.out.println("handler="+handler.getClass().getName());
        
        boolean simulatorEnabled = isSimulatorEnabled();
        
        if (request.getMethod().equals("POST") && simulatorEnabled) {
            super.preHandle(request, response, handler);
        }
        return true;
 
    }
    
    @Override
    protected void processUploads(HttpServletRequest request, Object handler, 
            Map<String,Resource> store){
        Map<String,String[]> parameterMap = request.getParameterMap();
        Iterator<String> iter = parameterMap.keySet().iterator();
        while( iter.hasNext() ){
            String key = iter.next();
            String[] parameterValues = parameterMap.get(key);
            if( parameterContainsSimulatorResource(parameterValues)){
                try {
                    String simulatedFile = parameterValues[0];
                    File newFile = File.createTempFile("sim-", 
                            simulatedFile.substring(simulatedFile.length() - 3));
                    newFile.deleteOnExit();
                    InputStream is = ICEmobileSimulatorResourceUploadInterceptor.class
                            .getClassLoader().getResourceAsStream(
                                "META-INF/web-resources/org.icefaces.component.skins/simulator/" +
                                simulatedFile );
                    FileOutputStream fos = new FileOutputStream(newFile);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    is.close();
                    String contentType = null;
                    if( simulatedFile.endsWith(".png")){
                        contentType = "image/png";
                    }
                    else if( simulatedFile.endsWith(".mp4")){
                        contentType = "video/mpeg";
                    }
                    else if( simulatedFile.endsWith(".mp3")){
                        contentType = "audio/mp3";
                    }
                    FileResource resource = new FileResource(newFile, contentType);
                    store.put(key, resource);
                } catch (IOException e) {
                   e.printStackTrace();
                }
                
            }
        }
    }
    
    /*
     * Return true if the simulator has been enabled in the web.xml
     */
    private boolean isSimulatorEnabled(){
        return "true".equals(servletContext.getInitParameter("org.icemobile.simulator"));
    }
    
    /*
     * Return true if the parameter value array contains a 
     * simulator upload resource name
     */
    private boolean parameterContainsSimulatorResource(String[] parameterValues){
        boolean result = false;
        for( int i = 0 ; i < parameterValues.length ; i++ ){
            if( parameterValues[i].startsWith(SIMULATOR_RESOURCE_KEY) ){
                result = true;
                break;
            }
        }
        return result;
    }

}
