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
