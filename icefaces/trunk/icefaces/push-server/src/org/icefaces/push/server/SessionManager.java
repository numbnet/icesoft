/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */
package org.icefaces.push.server;

import com.icesoft.faces.webapp.http.common.Configuration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
    private final Map freeMap = new HashMap();

    private final RequestManager requestManager;
    private final UpdatedViewsManager updatedViewsManager;

    private MessageService messageService;

    public SessionManager(
        final Configuration configuration,
        final MessageService messageService) {

        this.messageService = messageService;
        this.messageService.setCallback(
            BufferedContextEventsMessageHandler.Callback.class, this);
        this.messageService.setCallback(
            ContextEventMessageHandler.Callback.class, this);
        this.messageService.setCallback(
            UpdatedViewsMessageHandler.Callback.class, this);
        this.requestManager = new RequestManager();
        this.updatedViewsManager =
            new UpdatedViewsManager(configuration, messageService, this);
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public String getServletContextPath(final String iceFacesId) {
        synchronized (sessionMap) {
            if (sessionMap.containsKey(iceFacesId)) {
                return ((Session)sessionMap.get(iceFacesId)).servletContextPath;
            }
        }
        return null;
    }

    public UpdatedViewsManager getUpdatedViewsManager() {
        return updatedViewsManager;
    }

    public void iceFacesIdDisposed(
        final String servletContextPath, final String iceFacesId) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException exception) {
            // ignore interrupts.
        }
        synchronized (requestManager) {
            synchronized (freeMap) {
                // Marking the specified ICEfaces ID eligible for removal.
                freeMap.put(
                    iceFacesId,
                    new Record(iceFacesId, System.currentTimeMillis()));
                Handler _handler = requestManager.pull(iceFacesId);
                if (_handler != null) {
                    _handler.handle();
                }
            }
        }
    }

    public void iceFacesIdRetrieved(
        final String servletContextPath, final String iceFacesId) {

        synchronized (sessionMap) {
            cleanUp(iceFacesId);
            if (!sessionMap.containsKey(iceFacesId)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(
                        "ICEfaces ID retrieved: " + iceFacesId);
                }
                sessionMap.put(
                    iceFacesId, new Session(servletContextPath, iceFacesId));
            }
        }
    }

    public boolean hasViews(final Set iceFacesIdSet) {
        synchronized (sessionMap) {
            String[] _iceFacesIds =
                (String[])
                    iceFacesIdSet.toArray(new String[iceFacesIdSet.size()]);
            for (int i = 0; i < _iceFacesIds.length; i++) {
                if (hasViews(_iceFacesIds[i])) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean hasViews(final String iceFacesId) {
        synchronized (sessionMap) {
            return
                sessionMap.containsKey(iceFacesId) &&
                !((Session)sessionMap.get(iceFacesId)).viewNumberSet.isEmpty();
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
            synchronized (freeMap) {
                if (sessionMap.containsKey(iceFacesId) &&
                    !freeMap.containsKey(iceFacesId)) {

                    return true;
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(
                            "ICEfaces ID '" + iceFacesId + "' is not valid!");
                    }
                    return false;
                }
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
    public void sendUpdatedViews(final UpdatedViews updatedViews) {
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

    public void viewNumberDisposed(
        final String servletContextPath, final String iceFacesId,
        final String viewNumber) {

        synchronized (requestManager) {
            synchronized (sessionMap) {
                if (sessionMap.containsKey(iceFacesId)) {
                    Set _viewNumberSet =
                        ((Session)sessionMap.get(iceFacesId)).viewNumberSet;
                    if (_viewNumberSet.contains(viewNumber)) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(
                                "View Number disposed: " +
                                    viewNumber + " " +
                                        "[ICEfaces ID: " + iceFacesId + "]");
                        }
                        _viewNumberSet.remove(viewNumber);
                        updatedViewsManager.remove(iceFacesId, viewNumber);
                        if (!hasViews(iceFacesId)) {
                            Handler _handler = requestManager.pull(iceFacesId);
                            if (_handler != null) {
                                _handler.handle();
                            }
                        }
                    }
                }
            }
        }
    }

    public void viewNumberRetrieved(
        final String servletContextPath, final String iceFacesId,
        final String viewNumber) {

        synchronized (sessionMap) {
            if (sessionMap.containsKey(iceFacesId)) {
                Set _viewNumberSet =
                    ((Session)sessionMap.get(iceFacesId)).viewNumberSet;
                if (!_viewNumberSet.contains(viewNumber)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(
                            "View Number retrieved: " +
                                viewNumber + " " +
                                    "[ICEfaces ID: " + iceFacesId + "]");
                    }
                    _viewNumberSet.add(viewNumber);
                }
            }
        }
    }

    private void cleanUp(final String iceFacesId) {
        synchronized (freeMap) {
            synchronized (requestManager) {
                synchronized (sessionMap) {
                    Iterator _records = freeMap.values().iterator();
                    int _size = freeMap.size();
                    long _currentTime = System.currentTimeMillis();
                    for (int i = 0; i < _size; i++) {
                        Record _record = (Record)_records.next();
                        if (iceFacesId.equals(_record.iceFacesId)) {
                            // ICEfaces ID is still in use, remove from freeMap
                            _records.remove();
                        } else if (_currentTime - _record.timestamp >= 60000) {
                            if (sessionMap.containsKey(iceFacesId)) {
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug(
                                        "ICEfaces ID disposed: " + iceFacesId);
                                }
                                sessionMap.remove(iceFacesId);
                            }
                            updatedViewsManager.remove(iceFacesId);
                            _records.remove();
                        }
                    }
                }
            }
        }
    }

    private static class Record {
        private final String iceFacesId;
        private final long timestamp;

        private Record(final String iceFacesId, final long timestamp) {
            this.iceFacesId = iceFacesId;
            this.timestamp = timestamp;
        }
    }

    private static class Session {
        private final Set viewNumberSet = new HashSet();

        private String servletContextPath;
        private String iceFacesId;

        private Session(
            final String servletContextPath, final String iceFacesId) {

            this.servletContextPath = servletContextPath;
            this.iceFacesId = iceFacesId;
        }
    }
}
