package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.env.ServletEnvironmentRequest;
import com.icesoft.faces.webapp.xmlhttp.BlockingResponseState;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.ResponseStateManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

//todo: refactor this structure into an object with behavior
public class ServletView {
    private ServletExternalContext externalContext;
    private ServletFacesContext facesContext;
    private BlockingResponseState responseState;
    private PersistentFacesState persistentFacesState;
    private Map bundles;
    private ServletEnvironmentRequest wrappedRequest;
                                                                       
    public ServletView(String viewIdentifier, String sessionID, HttpServletRequest request, HttpServletResponse response, ResponseStateManager responseStateManager) {
        HttpSession session = request.getSession();
        ServletContext servletContext = session.getServletContext();
        wrappedRequest = new ServletEnvironmentRequest(request);
        externalContext = new ServletExternalContext(servletContext, wrappedRequest, response);
        //the call has the side effect of creating and setting up the state
        //todo: make this concept more visible and less subversive
        responseState = (BlockingResponseState) responseStateManager.getState(session, viewIdentifier);
        facesContext = new ServletFacesContext(externalContext, viewIdentifier, sessionID, responseState);
        persistentFacesState = new PersistentFacesState(facesContext, responseState);
        //collect bundles put by Tag components when the page is parsed
        bundles = externalContext.collectBundles();
    }

    public void setAsCurrentDuring(HttpServletRequest request, HttpServletResponse response) {
        externalContext.updateRequest(request);
        externalContext.updateResponse(response);
        externalContext.getRequestMap().putAll(bundles);
        persistentFacesState.setCurrentInstance();
        facesContext.setCurrentInstance();
    }

    public void switchToNormalMode() {
        facesContext.switchToNormalMode();
        externalContext.switchToNormalMode();
    }

    public void switchToPushMode() {
        facesContext.switchToPushMode();
        externalContext.switchToPushMode();
    }

    public void redirectIfRequired() throws IOException {
        // If the GET request handled by this servlet results in a
        // redirect (not likely under icefaces demo apps, but happens
        // all the time in Seam) then, we're stuck. We don't use the
        // X-REDIRECT mechanism in this servlet, so resort to some
        // good old fashioned manual redirect code.
        if (externalContext.redirectRequested()) {

            // Append 'rvn' parameter field to trigger new ServletView creation. Otherwise, Seam
            // based redirects wont find the new ViewId.
            ((HttpServletResponse) externalContext.getResponse()).sendRedirect(externalContext.redirectTo() +
                                             "&rvn="+facesContext.getViewNumber());
            externalContext.redirectComplete();
        }
    }

    public boolean differentURI(HttpServletRequest request) {
        return !request.getRequestURI().equals(wrappedRequest.getRequestURI());
    }

    public void release() {
        facesContext.release();
        persistentFacesState.release();        
    }
}