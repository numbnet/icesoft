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
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icemobile.application.ResourceStore;
import org.icemobile.util.Utils;

/**
 * This class can be used for ICEmobile simulator upload support. 
 * The class will intercept all requests checking for simulator upload
 * keys. If a simulator upload file key is found, the configured 
 * ICEmobileResourceUploadStore will be populated with the simulated file.
 * 
 * To configure your application for simulator uploads, include the 
 * following in your Spring configuration:
 * 
 * <interceptors>
 *    <beans:bean class="org.icemobile.spring.handler.SimulatorResourceUploadInterceptor" />
 * </interceptors>
 *
 */
public class SimulatorResourceUploadInterceptor 
    extends ResourceUploadInterceptor{
    
    private static final String SIMULATOR_RESOURCE_KEY = "sim-";
    
    /**
     * 
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return <code>true</code> if the execution chain should proceed with the
     * next interceptor or the handler itself. Else, DispatcherServlet assumes
     * that this interceptor has already dealt with the response itself.
     * @see    org.springframework.web.servlet.HandlerInterceptor 
     * @throws Exception in case of errors
     */
    public boolean preHandle(HttpServletRequest request, 
            HttpServletResponse response, Object handler)
        throws Exception {
        
        boolean simulatorEnabled = isSimulatorEnabled();
        
        if (request.getMethod().equals("POST") && simulatorEnabled) {
            super.preHandle(request, response, handler);
        }
        return true;
 
    }
    
    protected void processUploads(HttpServletRequest request, ResourceStore store){
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
                    InputStream is = SimulatorResourceUploadInterceptor.class
                            .getClassLoader().getResourceAsStream(
                                "META-INF/web-resources/org.icefaces.component.skins/simulator/" +
                                simulatedFile );
                    String suffix = simulatedFile.substring(simulatedFile.lastIndexOf("."));
                    String token = request.getSession().getId();
                    store.handleInputStream(is, Utils.CONTENT_TYPE_BY_FILE_EXT.get(suffix), key, token);
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
     * simulator upload resource name.
     * 
     * @param parameterValues The incoming parameter value array to check.
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
