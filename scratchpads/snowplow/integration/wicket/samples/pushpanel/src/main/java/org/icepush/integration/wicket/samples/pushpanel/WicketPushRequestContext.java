package org.icepush.integration.wicket.samples.pushpanel;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.icepush.PushContext;

/**
 * A WicketPushRequestContext is created for each PushPanel.
 */
public class WicketPushRequestContext implements Serializable{

    protected String currentPushId;

    public WicketPushRequestContext(WebRequest webRequest, WebResponse webResponse){

            intializePushContext((HttpServletRequest)webRequest.getHttpServletRequest(),
                            (HttpServletResponse)webResponse.getHttpServletResponse());
    }


    public String getCurrentPushId() {
            return currentPushId;
    }

    protected void intializePushContext(HttpServletRequest request,
                    HttpServletResponse response) throws IllegalArgumentException {
            if (request == null){
                throw new IllegalArgumentException("HttpServletRequest is null");
            }
            if (response == null){
                    throw new IllegalArgumentException("HttpServletResponse is null");
            }
            currentPushId = PushContext.getInstance(request.getSession().getServletContext()).createPushId(request, response);
    }
}
