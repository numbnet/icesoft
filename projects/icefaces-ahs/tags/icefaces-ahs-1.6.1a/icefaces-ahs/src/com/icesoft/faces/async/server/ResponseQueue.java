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

import java.util.Iterator;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResponseQueue {
    private static final Log LOG = LogFactory.getLog(ResponseQueue.class);

    private final String iceFacesId;
    private final String viewNumber;

    private ResponseQueueManager responseQueueManager;
    private int size;
    private double threshold;

    private final TreeSet responseQueue = new TreeSet();

    private int purgeCounter;

    private long sequenceNumber;

    public ResponseQueue(
        final String iceFacesId, final String viewNumber,
        final ResponseQueueManager responseQueueManager)
    throws IllegalArgumentException {
        if (iceFacesId == null) {
            throw new IllegalArgumentException("iceFacesId is null");
        }
        if (iceFacesId.trim().length() == 0) {
            throw new IllegalArgumentException("iceFacesId is empty");
        }
        if (viewNumber == null) {
            throw new IllegalArgumentException("viewNumber is null");
        }
        if (viewNumber.trim().length() == 0) {
            throw new IllegalArgumentException("viewNumber is empty");
        }
        this.iceFacesId = iceFacesId;
        this.viewNumber = viewNumber;
        if (responseQueueManager != null) {
            this.responseQueueManager = responseQueueManager;
            this.size = this.responseQueueManager.getResponseQueueSize();
            this.threshold =
                this.responseQueueManager.getResponseQueueThreshold();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Created " + this);
        }
    }

    public void add(final Response response)
    throws ResponseQueueExceededException {
        if (response != null &&
            response.getICEfacesID().equals(iceFacesId) &&
            response.getViewNumber().equals(viewNumber)) {

            if (responseQueueManager != null && getSize() == size) {
                throw new ResponseQueueExceededException();
            }
            if (responseQueue.isEmpty()) {
                responseQueue.add(response);
            } else if (!response.isEmpty()) {
                if (((Response)responseQueue.first()).isEmpty()) {
                    responseQueue.clear();
                }
                responseQueue.add(response);
            }
            long _sequenceNumber = response.getSequenceNumber();
            if (_sequenceNumber > sequenceNumber) {
                sequenceNumber = _sequenceNumber;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Response added to " + this);
            }
        }
    }

    public void addAll(final ResponseQueue responseQueue)
    throws ResponseQueueExceededException {
        if (responseQueue != null &&
            responseQueue.iceFacesId.equals(iceFacesId) &&
            responseQueue.viewNumber.equals(viewNumber)) {

            Iterator _responses = responseQueue.responseQueue.iterator();
            while (_responses.hasNext()) {
                add((Response)_responses.next());
            }
        }
    }

    public void clear() {
        responseQueue.clear();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Cleared " + this);
        }
    }

    public boolean contains(final Response response) {
        return responseQueue.contains(response);
    }

    public String getICEfacesID() {
        return iceFacesId;
    }

    public int getPurgeCounter() {
        return purgeCounter;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public int getSize() {
        return responseQueue.size();
    }

    public String getViewNumber() {
        return viewNumber;
    }

    public boolean isEmpty() {
        return responseQueue.isEmpty();
    }

    public Iterator iterator() {
        return responseQueue.iterator();
    }

    public void purge(final long sequenceNumber) {
        purge(sequenceNumber, false);
    }

    public void purge(
        final long sequenceNumber, final boolean ignorePurgeCounter) {

        if (sequenceNumber > 0) {
            Iterator _responses = responseQueue.iterator();
            while (_responses.hasNext()) {
                Response _response = (Response)_responses.next();
                if (_response.getSequenceNumber() > sequenceNumber) {
                    break;
                }
                _responses.remove();
                if (!ignorePurgeCounter) {
                    purgeCounter++;
                }
                if (responseQueueManager != null) {
                    responseQueueManager.purged(
                        iceFacesId, viewNumber, _response.getSequenceNumber());
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Purged up to " + sequenceNumber + " from " + this);
            }
            if (responseQueueManager != null &&
                purgeCounter > size * threshold) {

                responseQueueManager.publishPurgeMessage(
                    iceFacesId, viewNumber);
            }
        }
    }

    public void resetPurgeCounter() {
        purgeCounter = 0;
    }

    public String toString() {
        return
            "ResponseQueue [" + iceFacesId + "/" + viewNumber + "]:\r\n" +
            "        sequence number : " + sequenceNumber + "\r\n" +
            "        size            : " + getSize() + "\r\n" +
            "        purge counter   : " + purgeCounter;
    }
}
