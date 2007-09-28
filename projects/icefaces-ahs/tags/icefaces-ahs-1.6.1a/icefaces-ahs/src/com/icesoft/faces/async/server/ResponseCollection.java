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
package com.icesoft.faces.async.server;

import com.icesoft.faces.webapp.xmlhttp.Response;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResponseCollection {
    private static Log log = LogFactory.getLog(ResponseCollection.class);

    private final String iceFacesId;

    private final ResponseQueueManager responseQueueManager;

    private final Map responseMap = new HashMap();

    public ResponseCollection(
        String iceFacesId, ResponseQueueManager responseQueueManager)
    throws IllegalArgumentException {
        if (iceFacesId == null) {
            throw new IllegalArgumentException("iceFacesId is null");
        }
        if (iceFacesId.trim().length() == 0) {
            throw new IllegalArgumentException("iceFacesId is empty");
        }
        this.iceFacesId = iceFacesId;
        this.responseQueueManager = responseQueueManager;
        if (log.isDebugEnabled()) {
            log.debug("Created " + this);
        }
    }

    public void add(Response response)
    throws ResponseQueueExceededException {
        if (response != null && response.getICEfacesID().equals(iceFacesId)) {
            String _viewNumber = response.getViewNumber();
            getOrCreateResponseQueue(_viewNumber).add(response);
            if (log.isDebugEnabled()) {
                log.debug("Response added to " + this);
            }
        }
    }

    public void addAll(ResponseCollection responseCollection)
    throws ResponseQueueExceededException {
        if (responseCollection != null &&
            responseCollection.iceFacesId.equals(iceFacesId)) {

            Iterator _viewNumbers;
            _viewNumbers =
                responseCollection.responseMap.keySet().iterator();
            while (_viewNumbers.hasNext()) {
                addAll(
                    (ResponseQueue)
                        responseCollection.responseMap.get(
                            _viewNumbers.next()));
            }
        }
    }

    public void addAll(ResponseQueue responseQueue)
    throws ResponseQueueExceededException {
        if (responseQueue != null &&
            responseQueue.getICEfacesID().equals(iceFacesId)) {

            Iterator _responses = responseQueue.iterator();
            while (_responses.hasNext()) {
                add((Response)_responses.next());
            }
        }
    }

    public void clear() {
        Iterator _responseQueues = responseMap.values().iterator();
        while (_responseQueues.hasNext()) {
            ((ResponseQueue)_responseQueues.next()).clear();
        }
    }

    public boolean contains(Response response) {
        if (response == null || !response.getICEfacesID().equals(iceFacesId)) {
            return false;
        }
        Iterator _responseQueues = responseMap.values().iterator();
        while (_responseQueues.hasNext()) {
            if (((ResponseQueue)_responseQueues.next()).contains(response)) {
                return true;
            }
        }
        return false;
    }

    public String getICEfacesID() {
        return iceFacesId;
    }

    public ResponseQueue getResponseQueue(String viewNumber) {
        return (ResponseQueue)responseMap.get(viewNumber);
    }

    public int getSize() {
        int _size = 0;
        Iterator _responseQueues = responseMap.values().iterator();
        while (_responseQueues.hasNext()) {
            _size += ((ResponseQueue)_responseQueues.next()).getSize();
        }
        return _size;
    }

    public Set getViewNumberSet() {
        return responseMap.keySet();
    }

    public boolean isEmpty() {
        Iterator _responseQueues = responseMap.values().iterator();
        while (_responseQueues.hasNext()) {
            if (!((ResponseQueue)_responseQueues.next()).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void purge(String viewNumber, long sequenceNumber) {
        purge(viewNumber, sequenceNumber, false);
    }

    public void purge(
        String viewNumber, long sequenceNumber, boolean ignorePurgeCounter) {

        if (sequenceNumber > 0) {
            if (responseMap.containsKey(viewNumber)) {
                ((ResponseQueue)responseMap.get(viewNumber)).
                    purge(sequenceNumber, ignorePurgeCounter);
                if (log.isDebugEnabled()) {
                    log.debug(
                        "Purged up to " + sequenceNumber + " from " + this);
                }
            }
        }
    }

    public void resetPurgeCounter(String viewNumber) {
        if (responseMap.containsKey(viewNumber)) {
            ((ResponseQueue)responseMap.get(viewNumber)).resetPurgeCounter();
        }
    }

    public String toString() {
        return
            "ResponseCollection [" + iceFacesId + "]:\r\n" +
            "        size : " + getSize();
    }

    private ResponseQueue getOrCreateResponseQueue(String viewNumber) {
        ResponseQueue _responseQueue;
        if (responseMap.containsKey(viewNumber)) {
            _responseQueue = (ResponseQueue)responseMap.get(viewNumber);
        } else {
            _responseQueue =
                new ResponseQueue(iceFacesId, viewNumber, responseQueueManager);
            responseMap.put(viewNumber, _responseQueue);
        }
        return _responseQueue;
    }
}
