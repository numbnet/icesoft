package org.icemobile.jsp.util;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class Util {
	
    
    public static String getContextRoot(ServletRequest request){
    	return ((HttpServletRequest)request).getContextPath();
    }


}
