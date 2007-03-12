package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.env.ServletEnvironmentRequest;
import com.icesoft.faces.webapp.command.Command;
import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.command.NOOP;
import com.icesoft.faces.webapp.http.core.ViewQueue;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.context.BridgeFacesContext;
import com.icesoft.util.SeamUtilities;
import edu.emory.mathcs.backport.java.util.concurrent.locks.Lock;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

//todo: refactor this structure into an object with behavior
public class ServletView implements CommandQueue {
    private static final NOOP NOOP = new NOOP();
    private Lock lock = new ReentrantLock();
    private ServletExternalContext externalContext;
    private BridgeFacesContext facesContext;
    private ViewQueue allServedViews;
    private PersistentFacesState persistentFacesState;
    private Map bundles;
    private ServletEnvironmentRequest wrappedRequest;
    private Command currentCommand = NOOP;
    private String viewIdentifier;

    public ServletView(final String viewIdentifier, String sessionID, HttpServletRequest request, HttpServletResponse response, ViewQueue allServedViews) {
        HttpSession session = request.getSession();
        ServletContext servletContext = session.getServletContext();
        this.wrappedRequest = new ServletEnvironmentRequest(request);
        this.viewIdentifier = viewIdentifier;
        this.allServedViews = allServedViews;
        this.externalContext = new ServletExternalContext(viewIdentifier, servletContext, wrappedRequest, response, this);
        this.facesContext = new BridgeFacesContext(externalContext, viewIdentifier, sessionID, this);
        this.persistentFacesState = new PersistentFacesState(facesContext);
        //collect bundles put by Tag components when the page is parsed
        this.bundles = externalContext.collectBundles();
    }

    public void setAsCurrentDuring(HttpServletRequest request, HttpServletResponse response) {
        externalContext.update(request, response);
        externalContext.injectBundles(bundles);
        persistentFacesState.setCurrentInstance();
        facesContext.setCurrentInstance();
        facesContext.applyBrowserDOMChanges();
    }

    public void switchToNormalMode() {
        facesContext.switchToNormalMode();
        externalContext.switchToNormalMode();
    }

    public void switchToPushMode() {
        facesContext.switchToPushMode();
        externalContext.switchToPushMode();
    }

    /**
     * Check to see if the URI is different in any material (or Seam) way.
     *
     * @param request ServletRequest
     * @return true if the URI is considered different
     */
    public boolean differentURI(HttpServletRequest request) {

        // As a temporary fix, all GET requests are non-faces requests, and thus,
        // are considered different to force a new ViewRoot to be constructed.
        return (SeamUtilities.isSeamEnvironment()) ||
                !request.getRequestURI().equals(wrappedRequest.getRequestURI());
    }

    public void put(Command command) {
        lock.lock();
        currentCommand = currentCommand.coalesceWith(command);
        lock.unlock();
        try {
            allServedViews.put(viewIdentifier);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Command take() {
        lock.lock();
        Command command = currentCommand;
        currentCommand = NOOP;
        lock.unlock();

        return command;
    }

    public void release() {
        facesContext.release();
        persistentFacesState.release();
    }
}