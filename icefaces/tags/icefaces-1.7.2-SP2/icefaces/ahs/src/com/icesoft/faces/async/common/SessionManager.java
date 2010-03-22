package com.icesoft.faces.async.common;

import com.icesoft.faces.async.common.messaging.BufferedContextEventsMessageHandler;
import com.icesoft.faces.async.common.messaging.ContextEventMessageHandler;
import com.icesoft.faces.async.common.messaging.MessageService;
import com.icesoft.faces.async.common.messaging.UpdatedViewsMessageHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SessionManager
implements
    BufferedContextEventsMessageHandler.Callback,
    ContextEventMessageHandler.Callback,
    UpdatedViewsMessageHandler.Callback {

    private static final Log LOG = LogFactory.getLog(SessionManager.class);

    private final Map sessionMap = new HashMap();

    private final RequestManager requestManager;
    private final UpdatedViewsManager updatedViewsManager;

    private MessageService messageService;

    public SessionManager(
        final RequestManager requestManager,
        final UpdatedViewsManager updatedViewsManager,
        final MessageService messageService)
    throws IllegalArgumentException {
        if (requestManager == null) {
            throw new IllegalArgumentException("requestManager is null");
        }
        if (updatedViewsManager == null) {
            throw new IllegalArgumentException("updatedViewsManager is null");
        }
        if (messageService == null) {
            throw new IllegalArgumentException("messageService is null");
        }
        this.requestManager = requestManager;
        this.updatedViewsManager = updatedViewsManager;
        this.messageService = messageService;
        this.messageService.setCallback(
            BufferedContextEventsMessageHandler.Callback.class, this);
        this.messageService.setCallback(
            ContextEventMessageHandler.Callback.class, this);
        this.messageService.setCallback(
            UpdatedViewsMessageHandler.Callback.class, this);
    }

    public void contextDestroyed() {
        // do nothing.
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public UpdatedViewsManager getUpdatedViewsManager() {
        return updatedViewsManager;
    }

    public void iceFacesIdDisposed(final String iceFacesId) {
        // temporary go to sleep... when the AHS gets refactored we should be
        // able to fix this more properly.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException exception) {
            // ignore interrupts.
        }
        sessionDestroyed(iceFacesId);
    }

    public void iceFacesIdRetrieved(final String iceFacesId) {
        synchronized (sessionMap) {
            if (!sessionMap.containsKey(iceFacesId)) {
                sessionMap.put(iceFacesId, new HashSet());
            }
        }
    }

    public boolean isValid(final Set iceFacesIdSet) {
        synchronized (sessionMap) {
            String[] _iceFacesIds =
                (String[])
                    iceFacesIdSet.toArray(new String[iceFacesIdSet.size()]);
            for (int i = 0; i < _iceFacesIds.length; i++) {
                if (isValid(_iceFacesIds[i])) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isValid(final String iceFacesId) {
        synchronized (sessionMap) {
            if (sessionMap.containsKey(iceFacesId)) {
                return true;
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("ICEfaces ID '" + iceFacesId + "' is not valid!");
                }
                return false;
            }
        }
    }

    public boolean isValid(
        final String iceFacesId, final String[] viewNumbers) {

        synchronized (sessionMap) {
            if (isValid(iceFacesId)) {
                if (viewNumbers != null && viewNumbers.length != 0) {
                    Set _viewNumberSet = (Set)sessionMap.get(iceFacesId);
                    for (int i = 0; i < viewNumbers.length; i++) {
                        if (!_viewNumberSet.contains(viewNumbers[i])) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug(
                                    "View Number '" + viewNumbers[i] + "' " +
                                        "is not valid!");
                            }
                            return false;
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * <p>
     *   Send the specified <code>updatedViews</code> to the requester. If no
     *   pending request could be found for the requester, the
     *   <code>updatedViews</code> will be put into the queue, so it can be send
     *   to the requester as soon as a new request becomes available.
     * </p>
     *
     * @param      updatedViews
     *                 the updatedViews to be send to the requester.
     * @throws     IllegalArgumentException
     *                 if the specified <code>updatedViews</code> is
     *                 <code>null</code>.
     */
    public void sendUpdatedViews(final UpdatedViews updatedViews)
    throws IllegalArgumentException {
        // todo: refactor this!
        if (updatedViews == null) {
            throw new IllegalArgumentException("updatedViews is null");
        }
        synchronized (sessionMap) {
            if (isValid(updatedViews.getICEfacesID())) {
                updatedViewsManager.push(updatedViews);
                Handler _handler =
                    requestManager.pull(updatedViews.getICEfacesID());
                if (_handler != null) {
                    _handler.handle();
                }
            }
        }
    }

    public void sessionDestroyed(final String iceFacesId) {
        synchronized (requestManager) {
            synchronized (sessionMap) {
                if (sessionMap.containsKey(iceFacesId)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(
                            "Session Destroyed: " + iceFacesId);
                    }
                    sessionMap.remove(iceFacesId);
                }
                Handler _handler = requestManager.pull(iceFacesId);
                if (_handler != null) {
                    _handler.handle();
                }
                updatedViewsManager.remove(iceFacesId);
            }
        }
    }

    public void viewNumberRetrieved(
        final String iceFacesId, final String viewNumber) {

        synchronized (sessionMap) {
            if (sessionMap.containsKey(iceFacesId)) {
                Set _viewNumberSet =
                    (Set)sessionMap.get(iceFacesId);
                if (!_viewNumberSet.contains(viewNumber)) {
                    _viewNumberSet.add(viewNumber);
                }
            }
        }
    }
}
