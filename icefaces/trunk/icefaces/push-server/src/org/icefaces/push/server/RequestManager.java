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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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

import edu.emory.mathcs.backport.java.util.concurrent.ScheduledThreadPoolExecutor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RequestManager {
    private static final Log LOG = LogFactory.getLog(RequestManager.class);

    private final Map pendingRequestMap = new HashMap();

    public RequestManager() {
        // Do nothing.
    }

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
            Iterator _iceFacesIds = iceFacesIdSet.iterator();
            while (_iceFacesIds.hasNext()) {
                Handler _handler = pull((String)_iceFacesIds.next());
                if (_handler != null) {
                    return _handler;
                }
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
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(
                            "Unparked pending request: " +
                                "ICEfaces ID set [" + _entry.getKey() + "]");
                    }
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
                LOG.debug(
                    "Parked pending request: " +
                        "ICEfaces ID set [" + iceFacesIdSet + "]");
            }
        }
    }
}
