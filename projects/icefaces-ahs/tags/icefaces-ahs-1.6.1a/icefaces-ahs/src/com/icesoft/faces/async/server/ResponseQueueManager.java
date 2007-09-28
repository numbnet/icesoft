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
import com.icesoft.util.net.messaging.MessageServiceClient;
import com.icesoft.util.net.messaging.Message;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResponseQueueManager {
    private static final String PURGE_MESSAGE_TYPE = "Purge";
    private static final String RESPONSE_MESSAGE_TYPE = "Response";

    private static final Log LOG =
        LogFactory.getLog(ResponseQueueManager.class);

    private final AsyncHttpServer asyncHttpServer;

    private final Map responseQueueMap = new HashMap();
    private final Map purgeMap = new HashMap();

    private int responseQueueSize;
    private double responseQueueThreshold;
    private String purgeMessageContents;

    public ResponseQueueManager(final AsyncHttpServer asyncHttpServer)
    throws IllegalArgumentException {
        if (asyncHttpServer == null) {
            throw new IllegalArgumentException("asyncHttpServer is null");
        }
        this.asyncHttpServer = asyncHttpServer;
    }

    public ResponseCollection pull(
        final String iceFacesId, final String[] viewNumbers,
        final String[][] sequenceNumbers) {

        if (iceFacesId == null || iceFacesId.trim().length() == 0 ||
            viewNumbers == null || viewNumbers.length == 0) {

            return null;
        }
        synchronized (responseQueueMap) {
            if (responseQueueMap.containsKey(iceFacesId)) {
                ResponseCollection _responseCollection =
                    new ResponseCollection(iceFacesId, null);
                ResponseCollection _tempResponseCollection =
                    (ResponseCollection)responseQueueMap.get(iceFacesId);
                for (int i = 0; i < viewNumbers.length; i++) {
                    ResponseQueue _responseQueue =
                        _tempResponseCollection.getResponseQueue(
                            viewNumbers[i]);
                    if (_responseQueue != null) {
                        long _sequenceNumber = 0;
                        if (sequenceNumbers != null) {
                            for (int j = 0; j < sequenceNumbers.length; j++) {
                                if (sequenceNumbers[j][0].equals(
                                        viewNumbers[i])) {

                                    _sequenceNumber =
                                        Long.parseLong(sequenceNumbers[j][1]);
                                }
                            }
                        }
                        _responseQueue.purge(_sequenceNumber);
                        try {
                            _responseCollection.addAll(_responseQueue);
                        } catch (ResponseQueueExceededException exception) {
                            if (LOG.isErrorEnabled()) {
                                LOG.error(
                                    "ResponseQueue exceeded for " +
                                        iceFacesId + "!",
                                    exception);
                            }
                        }
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(
                                "Pulled pending response: " +
                                    iceFacesId + "/" + viewNumbers[i]);
                        }
                    }
                }
                return
                    !_responseCollection.isEmpty() ? _responseCollection : null;
            }
        }
        return null;
    }

    public void purge(
        final String iceFacesId, final String viewNumber,
        final long sequenceNumber) {

        purge(iceFacesId, viewNumber, sequenceNumber, false);
    }

    public void purge(
        final String iceFacesId, final String viewNumber,
        final long sequenceNumber, final boolean ignorePurgeCounter) {

        synchronized (responseQueueMap) {
            if (responseQueueMap.containsKey(iceFacesId)) {
                ((ResponseCollection)responseQueueMap.get(iceFacesId)).
                    purge(viewNumber, sequenceNumber, ignorePurgeCounter);
            }
        }
    }

    public void purgeAll(final Map purgeMap) {
        synchronized (responseQueueMap) {
            Iterator _iceFacesIds = purgeMap.keySet().iterator();
            while (_iceFacesIds.hasNext()) {
                String _iceFacesId = (String)_iceFacesIds.next();
                Map _viewNumberMap = (Map)purgeMap.get(_iceFacesId);
                Iterator _viewNumbers = _viewNumberMap.keySet().iterator();
                while (_viewNumbers.hasNext()) {
                    String _viewNumber = (String)_viewNumbers.next();
                    long _sequenceNumber =
                        ((Long)_viewNumberMap.get(_viewNumber)).longValue();
                    purge(_iceFacesId, _viewNumber, _sequenceNumber, true);
                    resetPurgeCounter(_iceFacesId, _viewNumber);
                }
            }
        }
    }

    public void push(final Response response)
    throws ResponseQueueExceededException {
        if (response == null) {
            return;
        }
        synchronized (responseQueueMap) {
            String _iceFacesId = response.getICEfacesID();
            String _viewNumber = response.getViewNumber();
            ResponseCollection _responseCollection;
            if (responseQueueMap.containsKey(_iceFacesId)) {
                _responseCollection =
                    (ResponseCollection)responseQueueMap.get(_iceFacesId);
            } else {
                _responseCollection =
                    new ResponseCollection(response.getICEfacesID(), this);
                responseQueueMap.put(_iceFacesId, _responseCollection);
            }
            try {
                _responseCollection.add(response);
            } catch (ResponseQueueExceededException exception) {
                _responseCollection.getResponseQueue(_viewNumber).clear();
                throw exception;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug(
                    "Pushed pending response: " +
                        _iceFacesId + "/" + _viewNumber + " " +
                            "[size: " +
                                _responseCollection.
                                    getResponseQueue(_viewNumber).getSize() +
                            "]");
            }
        }
    }

    public void remove(final String iceFacesId) {
        synchronized (responseQueueMap) {
            if (responseQueueMap.containsKey(iceFacesId)) {
                responseQueueMap.remove(iceFacesId);
            }
        }
    }

    public void resetPurgeCounter(
        final String iceFacesId, final String viewNumber) {

        synchronized (responseQueueMap) {
            if (responseQueueMap.containsKey(iceFacesId)) {
                ((ResponseCollection)responseQueueMap.get(iceFacesId)).
                    resetPurgeCounter(viewNumber);
            }
        }
    }

    String getPurgeMessageContents() {
        return purgeMessageContents;
    }

    int getResponseQueueSize() {
        return responseQueueSize;
    }

    double getResponseQueueThreshold() {
        return responseQueueThreshold;
    }

    void publishPurgeMessage(final String iceFacesId, final String viewNumber) {
        StringBuffer _purgeMessage = new StringBuffer();
        synchronized (purgeMap) {
            if (purgeMessageContents.equalsIgnoreCase("all")) {
                Iterator _iceFacesIds = purgeMap.keySet().iterator();
                while (_iceFacesIds.hasNext()) {
                    String _iceFacesId = (String)_iceFacesIds.next();
                    _purgeMessage.append(_iceFacesId);
                    Map _viewNumberMap = (Map)purgeMap.get(_iceFacesId);
                    Iterator _viewNumbers = _viewNumberMap.keySet().iterator();
                    while (_viewNumbers.hasNext()) {
                        String _viewNumber = (String)_viewNumbers.next();
                        _purgeMessage.append(";");
                        _purgeMessage.append(_viewNumber);
                        _purgeMessage.append(";");
                        _purgeMessage.append(_viewNumberMap.get(_viewNumber));
                    }
                    _purgeMessage.append("\r\n");
                }
            } else if (purgeMessageContents.equalsIgnoreCase("session")) {
                if (purgeMap.containsKey(iceFacesId)) {
                    Map _viewNumberMap = (Map)purgeMap.get(iceFacesId);
                    Iterator _viewNumbers = _viewNumberMap.keySet().iterator();
                    if (_viewNumbers.hasNext()) {
                        _purgeMessage.append(iceFacesId);
                        do {
                            String _viewNumber = (String)_viewNumbers.next();
                            _purgeMessage.append(";");
                            _purgeMessage.append(_viewNumber);
                            _purgeMessage.append(";");
                            _purgeMessage.append(
                                _viewNumberMap.get(_viewNumber));
                        } while (_viewNumbers.hasNext());
                        _purgeMessage.append("\r\n");
                    }
                }
            } else if (purgeMessageContents.equalsIgnoreCase("single")) {
                if (purgeMap.containsKey(iceFacesId)) {
                    Map _viewNumberMap = (Map)purgeMap.get(iceFacesId);
                    if (_viewNumberMap.containsKey(viewNumber)) {
                        _purgeMessage.append(iceFacesId);
                        _purgeMessage.append(";");
                        _purgeMessage.append(viewNumber);
                        _purgeMessage.append(";");
                        _purgeMessage.append(_viewNumberMap.get(viewNumber));
                        _purgeMessage.append("\r\n");
                    }
                }
            }
        }
        if (_purgeMessage.length() != 0) {
            asyncHttpServer.getMessageServiceClient().publish(
                _purgeMessage.toString(),
                null,
                PURGE_MESSAGE_TYPE,
                MessageServiceClient.RESPONSE_TOPIC_NAME);
            /*
             * todo: maybe we should start thinking about sending Objects
             *       instead of Strings.
             */
//            synchronized (purgeMap) {
//                asyncHttpServer.getMessageServiceClient().publish(
//                    purgeMap,
//                    null,
//                    PURGE_MESSAGE_TYPE,
//                    MessageServiceClient.RESPONSE_TOPIC_NAME);
//            }
        }
    }

    void publishResponseQueues(final String destinationNodeAddress) {
        synchronized (responseQueueMap) {
            MessageServiceClient _messageServiceClient =
                asyncHttpServer.getMessageServiceClient();
            Properties _messageProperties = new Properties();
            _messageProperties.setProperty(
                Message.DESTINATION_NODE_ADDRESS, destinationNodeAddress);
            Iterator _iceFacesIds = responseQueueMap.keySet().iterator();
            while (_iceFacesIds.hasNext()) {
                String _iceFacesId = (String)_iceFacesIds.next();
                ResponseCollection _responseCollection =
                    (ResponseCollection)responseQueueMap.get(_iceFacesId);
                Iterator _viewNumbers =
                    _responseCollection.getViewNumberSet().iterator();
                while (_viewNumbers.hasNext()) {
                    String _viewNumber = (String)_viewNumbers.next();
                    ResponseQueue _responseQueue =
                        _responseCollection.getResponseQueue(_viewNumber);
                    Iterator _responses = _responseQueue.iterator();
                    while (_responses.hasNext()) {
                        Response _response = (Response)_responses.next();
                        _messageServiceClient.publish(
                            _iceFacesId + ";" +
                                _viewNumber + ";" +
                                _response.getSequenceNumber() + ";" +
                                _response.getEntityBody(),
                            _messageProperties,
                            RESPONSE_MESSAGE_TYPE,
                            MessageServiceClient.RESPONSE_TOPIC_NAME
                        );
                    }
                }
            }
        }
        /*
         * todo: maybe we should start thinking about sending Objects instead of
         *       Strings.
         */
    }

    void purged(
        final String iceFacesId, final String viewNumber,
        final long sequenceNumber) {

        if (iceFacesId != null && iceFacesId.trim().length() != 0 &&
            viewNumber != null && viewNumber.trim().length() != 0 &&
            sequenceNumber > 0) {

            synchronized (purgeMap) {
                Map _viewNumberMap;
                if (purgeMap.containsKey(iceFacesId)) {
                    _viewNumberMap = (Map)purgeMap.get(iceFacesId);
                } else {
                    _viewNumberMap = new HashMap();
                    purgeMap.put(iceFacesId, _viewNumberMap);
                }
                if (!_viewNumberMap.containsKey(viewNumber) ||
                    sequenceNumber >
                        ((Long)_viewNumberMap.get(viewNumber)).longValue()) {

                    _viewNumberMap.put(viewNumber, new Long(sequenceNumber));
                }
            }
        }
    }

    void setPurgeMessageContents(final String purgeMessageContents) {
        this.purgeMessageContents = purgeMessageContents;
    }

    void setResponseQueueSize(final int responseQueueSize) {
        this.responseQueueSize = responseQueueSize;
    }

    void setResponseQueueThreshold(final double responseQueueThreshold) {
        this.responseQueueThreshold = responseQueueThreshold;
    }
}
