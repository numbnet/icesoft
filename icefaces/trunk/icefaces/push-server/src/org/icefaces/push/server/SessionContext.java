package org.icefaces.push.server;

import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SessionContext {
    private static final Log LOG = LogFactory.getLog(SessionContext.class);

    private final Set viewNumberSet = new HashSet();

    private final ReentrantLock lock = new ReentrantLock();

    private String servletContextPath;
    private String iceFacesId;

    public SessionContext(final String servletContextPath, final String iceFacesId) {
        this.servletContextPath = servletContextPath;
        this.iceFacesId = iceFacesId;
    }

    public void addViewNumber(final String viewNumber) {
        if (!viewNumberSet.contains(viewNumber)) {
            viewNumberSet.add(viewNumber);
        }
    }

    public boolean hasViewNumber(final String viewNumber) {
        return viewNumberSet.contains(viewNumber);
    }

    public boolean hasViewNumbers() {
        return !viewNumberSet.isEmpty();
    }

    public String getICEfacesID() {
        return iceFacesId;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public String getServletContextPath() {
        return servletContextPath;
    }

    public void removeViewNumber(final String viewNumber) {
        if (viewNumberSet.contains(viewNumber)) {
            viewNumberSet.remove(viewNumber);
        }
    }
}
