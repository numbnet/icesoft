package org.icepush.samples.icechat.auth;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class SecurityFilter implements Filter {
	
	private String loginURL;
	private static final String LOGIN_URL_KEY = "loginURL";
	private FilterConfig filterConfig;
	
	private static Logger LOG = Logger.getLogger(SecurityFilter.class.getName());
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		
		if( httpRequest.getRequestURI().contains(loginURL)){
			chain.doFilter(request, response);
		}
		else if( httpRequest.getSession(false) == null || 
				httpRequest.getSession(true).getAttribute(SecurityServlet.USER_KEY) == null ){
			LOG.info("unauthorized request '" + httpRequest.getRequestURI() + "', directing to " + loginURL);
			filterConfig.getServletContext().getRequestDispatcher(loginURL).forward(request,response);
		}
		else{
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {		
		this.filterConfig = filterConfig;
		loginURL = filterConfig.getServletContext().getInitParameter(LOGIN_URL_KEY);
	}

}
