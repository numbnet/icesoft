package com.icesoft.applications.faces.auctionMonitor;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import java.io.IOException;
import javax.servlet.ServletException;
import com.icesoft.faces.application.ViewRootStateManagerImpl;

public class StateManagerFilter implements Filter {
    private FilterConfig filterConfig;
    private String stateStrategy;

    public void init(FilterConfig filterConfig)  {
        this.filterConfig = filterConfig;
        stateStrategy = filterConfig.getInitParameter(
                ViewRootStateManagerImpl.STATE_STRATEGY);
    }


    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        if (null != stateStrategy)  {
            request.setAttribute(ViewRootStateManagerImpl.STATE_STRATEGY, 
                    stateStrategy);
        }
        chain.doFilter (request, response);

    }

    public void destroy()  {
    }
}


