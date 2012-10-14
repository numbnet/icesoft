package org.icemobile.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class EnvUtils {
    
    public static boolean isProductionStage(HttpServletRequest request){
        String param = getContextParam(request.getSession().getServletContext(), Constants.PROJECT_STAGE_PARAM);
        return "production".equalsIgnoreCase(param);
    }
    
    private static String getContextParam(ServletContext servletContext, String param){
        return servletContext.getInitParameter(param);
    }

}
