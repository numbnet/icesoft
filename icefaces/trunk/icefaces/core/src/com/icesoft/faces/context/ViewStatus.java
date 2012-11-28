package com.icesoft.faces.context;

import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.servlet.MainSessionBoundServlet;

import java.net.URI;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ViewStatus {
    private static final Log LOG = LogFactory.getLog(ViewStatus.class);

    private static final String VIEW_STATUS_KEY = "com.icesoft.faces.ViewStatus";

    private HashMap viewRecords;
    private long viewsCreated = 0;
    private long viewsDisposed = 0;

    public ViewStatus() {
        viewRecords = new HashMap();
    }

    public void recordViewCreated(String viewIdentifier, URI uri) {
        viewRecords.put(viewIdentifier,uri.toString());
        viewsCreated++;
    }

    public void recordViewDisposed(String viewIdentifier) {
        viewRecords.remove(viewIdentifier);
        viewsDisposed++;
    }

    public String getCurrentStatus(String sessionId){
        StringBuffer buff = new StringBuffer();
        buff.append("views for  ").append(sessionId).append("\n");
        buff.append("  created : ").append(viewsCreated).append("\n");
        buff.append("  disposed: ").append(viewsDisposed).append("\n");
        buff.append("  active  : ").append(viewRecords.size()).append("\n").append(viewRecords);
        return buff.toString();
    }

    public static ViewStatus getInstance(final HttpSession session) {
        ViewStatus viewStatus;
        Object obj = session.getAttribute(VIEW_STATUS_KEY);
        if(obj == null){
            viewStatus = new ViewStatus();
            session.setAttribute(VIEW_STATUS_KEY, viewStatus);
        } else {
            viewStatus = (ViewStatus)obj;
        }
        return viewStatus;
    }

    public HashMap getViewRecords() {
        return viewRecords;
    }

    public long getViewsCreated() {
        return viewsCreated;
    }

    public long getViewsDisposed() {
        return viewsDisposed;
    }

    public static void log(final HttpSession session) {
        if (LOG.isInfoEnabled()) {
            ViewStatus viewStatus = getInstance(session);
            if(viewStatus != null){
                String key = MainSessionBoundServlet.class.getName();
                String id = (String)session.getAttribute(key);
                LOG.info(viewStatus.getCurrentStatus(id));
            }
        }
    }

    public static void recordViewCreation(
        final HttpSession session, final Request request, final String sessionID, final String viewIdentifier) {

        if (LOG.isDebugEnabled())  {
            LOG.debug("created View[" + sessionID + ":" + viewIdentifier + "] for " + request.getURI());
            getInstance(session).recordViewCreated(viewIdentifier, request.getURI());
        }
    }

    public static void recordViewDisposal(
        final HttpSession session, final String sessionID, final String viewIdentifier){

        if (LOG.isDebugEnabled())  {
            LOG.debug("disposed View[" + sessionID + ":" + viewIdentifier + "]");
            getInstance(session).recordViewDisposed(viewIdentifier);
        }
    }
}
