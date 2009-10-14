package org.icepush;

import javax.servlet.http.HttpServletRequest;

public class PushContextLocator {

    public static synchronized PushContext getInstance(HttpServletRequest request) {
        //the returned PushContext must be aware of the BROWSERID cookie and
        //must have a reference to the application scope notification service
        //request.getSession().getServletContext().getAttribute(NOTIFY_SERVICE);
        return (PushContext) request.getSession().getAttribute(PushContext.class.getName());
    }
}
