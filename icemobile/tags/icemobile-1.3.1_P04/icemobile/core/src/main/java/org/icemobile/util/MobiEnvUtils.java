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

package org.icemobile.util;

import javax.servlet.ServletContext;

public class MobiEnvUtils {
    
    public static boolean isProductionStage(ServletContext servletContext)  {
        String param = getProjectStageParam(servletContext);
        
        return "production".equalsIgnoreCase(param);
    }

    public static boolean isDevelopmentStage(ServletContext servletContext)  {
        String param = getProjectStageParam(servletContext);
        
        return "development".equalsIgnoreCase(param);
    }
    
    private static String getProjectStageParam(ServletContext servletContext)  {
        String param = getContextParam(servletContext, 
                Constants.PROJECT_STAGE_PARAM);
        if ((null == param) || "".equals(param))  {
            param = System.getProperty(Constants.PROJECT_STAGE_PARAM);
        }
        //default to production
        if ((null == param) || "".equals(param))  {
            param = "production";
        }
        return param;
    }
    
    private static String getContextParam(ServletContext servletContext, 
            String param)  {
        return servletContext.getInitParameter(param);
    }

}
