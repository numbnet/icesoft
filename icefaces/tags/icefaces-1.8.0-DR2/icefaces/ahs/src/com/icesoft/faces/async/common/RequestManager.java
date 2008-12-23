package com.icesoft.faces.async.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RequestManager {
    private static final Log LOG = LogFactory.getLog(RequestManager.class);

    private final Map pendingRequestMap = new HashMap();

    /**
     * <p>
     *   Pulls the pending request, represented as a handler, for the specified
     *   <code>iceFacesId</code> off the queue.
     * </p>
     * <p>
     *   If <code>iceFacesId</code> is <code>null</code> or empty, or if there
     *   is no request in the queue for the <code>iceFacesId</code>,
     *   <code>null</code> is returned.
     * </p>
     *
     * @param      iceFacesIdSet
     *                 the ICEfaces ID set that identifies the session of the
     *                 requester.
     * @return     the request or <code>null</code>.
     * @see        #push(Set, Handler)
     */
    public Handler pull(final Set iceFacesIdSet) {
        if (iceFacesIdSet == null || iceFacesIdSet.isEmpty()) {
            return null;
        }
        synchronized (pendingRequestMap) {
            if (pendingRequestMap.containsKey(iceFacesIdSet)) {
                final Handler _handler =
                    (Handler)pendingRequestMap.remove(iceFacesIdSet);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Pulled pending request: " + iceFacesIdSet);
                }
                return _handler;
            }
            return null;
        }
    }

    public Handler pull(final String iceFacesId) {
        if (iceFacesId == null || iceFacesId.trim().length() == 0) {
            return null;
        }
        synchronized (pendingRequestMap) {
            Iterator _entries = pendingRequestMap.entrySet().iterator();
            while (_entries.hasNext()) {
                Map.Entry _entry = (Map.Entry)_entries.next();
                if (((Set)_entry.getKey()).contains(iceFacesId)) {
                    _entries.remove();
                    return (Handler)_entry.getValue();
                }
            }
            return null;
        }
    }

    /**
     * <p>
     *   Pushes the specified <code>handler</code>, representing the request,
     *   for the specified <code>iceFacesId</code> on the queue.
     * </p>
     * <p>
     *   If <code>iceFacesId</code> is <code>null</code> or empty, or
     *   <code>handler</code> is <code>null</code>, nothing is pushed on the
     *   queue.
     * </p>
     *
     * @param      iceFacesIdSet
     *                 the ICEfaces ID set that identifies the session of the
     *                 requester.
     * @param      handler
     *                 the handler that represents the pending request.
     * @see        #pull(Set)
     */
    public void push(final Set iceFacesIdSet, final Handler handler) {
        if (iceFacesIdSet == null || iceFacesIdSet.isEmpty() ||
            handler == null) {

            return;
        }
        synchronized (pendingRequestMap) {
            pendingRequestMap.put(iceFacesIdSet, handler);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Pushed pending request: " + iceFacesIdSet);
            }
        }
    }
}
