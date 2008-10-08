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
package com.icesoft.faces.async.common;

import com.icesoft.faces.async.common.messaging.MessageService;
import com.icesoft.faces.async.common.messaging.PurgeMessageHandler;
import com.icesoft.faces.async.common.messaging.AnnouncementMessageHandler;
import com.icesoft.faces.async.servlet.UpdatedViewsQueueExceededException;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.net.messaging.Message;
import com.icesoft.net.messaging.MessageServiceClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UpdatedViewsManager
implements AnnouncementMessageHandler.Callback, PurgeMessageHandler.Callback {
    private static final String PURGE_MESSAGE_TYPE = "Purge";
    private static final String UPDATED_VIEWS_MESSAGE_TYPE = "UpdatedViews";
    private static final String UPDATED_VIEWS_QUEUE_EXCEEDED_MESSAGE_TYPE =
        "UpdatedViewsQueueExceeded";

    private static final Log LOG =
        LogFactory.getLog(UpdatedViewsManager.class);

    private final MessageService messageService;
    private final Map purgeMap = new HashMap();
    private final Map updatedViewsQueueMap = new HashMap();

    private String purgeMessageContents;
    private int updatedViewsQueueSize;
    private double updatedViewsQueueThreshold;

    public UpdatedViewsManager(
        final Configuration configuration, final MessageService messageService)
    throws IllegalArgumentException {
        if (messageService == null) {
            throw new IllegalArgumentException("asyncHttpServer is null");
        }
        setPurgeMessageContents(
            configuration.getAttribute(
                "purgeMessageContents", "all"));
        setUpdatedViewsQueueSize(
            configuration.getAttributeAsInteger(
                "updatedViewsQueueSize", 100));
        setUpdatedViewsQueueThreshold(
            configuration.getAttributeAsDouble(
                "updatedViewsQueueThreshold", 0.7d));
        this.messageService = messageService;
        this.messageService.setCallback(
            AnnouncementMessageHandler.Callback.class, this);
        this.messageService.setCallback(
            PurgeMessageHandler.Callback.class, this);
    }

    public String getPurgeMessageContents() {
        return purgeMessageContents;
    }

    public int getUpdatedViewsQueueSize() {
        return updatedViewsQueueSize;
    }

    public double getUpdatedViewsQueueThreshold() {
        return updatedViewsQueueThreshold;
    }
    
    public void publishUpdatedViewsQueues(final String destinationNodeAddress) {
        synchronized (updatedViewsQueueMap) {
            Properties _messageProperties = new Properties();
            _messageProperties.setProperty(
                Message.DESTINATION_NODE_ADDRESS, destinationNodeAddress);
            Iterator _iceFacesIds = updatedViewsQueueMap.keySet().iterator();
            while (_iceFacesIds.hasNext()) {
                String _iceFacesId = (String)_iceFacesIds.next();
                UpdatedViewsQueue _updatedViewsQueue =
                    (UpdatedViewsQueue)updatedViewsQueueMap.get(_iceFacesId);
                Iterator _updatedViewsIterator = _updatedViewsQueue.iterator();
                while (_updatedViewsIterator.hasNext()) {
                    UpdatedViews _updatedViews =
                            (UpdatedViews)_updatedViewsIterator.next();
                    StringBuffer _stringBuffer = new StringBuffer();
                    Iterator _updatedViewIterator =
                        _updatedViews.getUpdatedViewsSet().iterator();
                    while (_updatedViewIterator.hasNext()) {
                        if (_stringBuffer.length() != 0) {
                            _stringBuffer.append(',');
                        }
                        _stringBuffer.append(_updatedViewIterator.next());
                    }
                    messageService.publish(
                        _iceFacesId + ";" +
                            _updatedViews.getSequenceNumber() + ";" +
                            _stringBuffer.toString(),
                        _messageProperties,
                        UPDATED_VIEWS_MESSAGE_TYPE,
                        MessageServiceClient.RESPONSE_TOPIC_NAME);
                }
            }
        }
    }

    public List pull(
        final Set iceFacesIdSet, final SequenceNumbers sequenceNumbers) {

        if (iceFacesIdSet == null || iceFacesIdSet.isEmpty()) {
            return null;
        }
        synchronized (updatedViewsQueueMap) {
            List _updatedViewsList = new ArrayList();
            Iterator _iceFacesIds = iceFacesIdSet.iterator();
            int _size = iceFacesIdSet.size();
            for (int i = 0; i < _size; i++) {
                String _iceFacesId = (String)_iceFacesIds.next();
                if (updatedViewsQueueMap.containsKey(_iceFacesId)) {
                    UpdatedViewsQueue _updatedViewsQueue =
                        (UpdatedViewsQueue)
                            updatedViewsQueueMap.get(_iceFacesId);
                    Long _sequenceNumber = sequenceNumbers.get(_iceFacesId);
                    if (_sequenceNumber != null) {
                        _updatedViewsQueue.purge(_sequenceNumber.longValue());
                    }
                    UpdatedViews _updatedViews;
                    if (!_updatedViewsQueue.isEmpty()) {
                        Iterator _updatedViewsQueueIterator =
                            _updatedViewsQueue.iterator();
                        _updatedViews =
                            (UpdatedViews)_updatedViewsQueueIterator.next();
                        while (_updatedViewsQueueIterator.hasNext()) {
                            _updatedViews =
                                UpdatedViews.merge(
                                    _updatedViews,
                                    (UpdatedViews)
                                        _updatedViewsQueueIterator.next());
                        }
                        _updatedViewsList.add(_updatedViews);
                    }
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Pulled pending updated views: " + iceFacesIdSet);
            }
            return _updatedViewsList;
        }
    }

    public void purge(final String iceFacesId, final long sequenceNumber) {
        if (iceFacesId == null || iceFacesId.trim().length() == 0) {
            return;
        }
        purge(iceFacesId, sequenceNumber, false);
    }

    public void purge(
        final String iceFacesId, final long sequenceNumber,
        final boolean ignorePurgeCounter) {

        if (iceFacesId == null || iceFacesId.trim().length() == 0) {
            return;
        }
        synchronized (updatedViewsQueueMap) {
            if (updatedViewsQueueMap.containsKey(iceFacesId)) {
                ((UpdatedViewsQueue)updatedViewsQueueMap.get(iceFacesId)).
                    purge(sequenceNumber, ignorePurgeCounter);
            }
        }
    }

    public void purgeUpdatedViews(Map purgeMap) {
        synchronized (updatedViewsQueueMap) {
            Iterator _iceFacesIds = purgeMap.keySet().iterator();
            while (_iceFacesIds.hasNext()) {
                String _iceFacesId = (String)_iceFacesIds.next();
                purge(
                    _iceFacesId, ((Long)purgeMap.get(_iceFacesId)).longValue());
                resetPurgeCounter(_iceFacesId);
            }
        }
    }

    public void push(final UpdatedViews updatedViews) {
        if (updatedViews != null) {
            String _iceFacesId = updatedViews.getICEfacesID();
            synchronized (updatedViewsQueueMap) {
                UpdatedViewsQueue _updatedViewsQueue;
                if (updatedViewsQueueMap.containsKey(_iceFacesId)) {
                    _updatedViewsQueue =
                        (UpdatedViewsQueue)
                            updatedViewsQueueMap.get(_iceFacesId);
                } else {
                    _updatedViewsQueue =
                        new UpdatedViewsQueue(_iceFacesId, this);
                    updatedViewsQueueMap.put(_iceFacesId, _updatedViewsQueue);
                }
                try {
                    _updatedViewsQueue.add(updatedViews);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(
                            "Pushed pending updated views: " +
                                _iceFacesId + " " +
                                    "[size: " +
                                        _updatedViewsQueue.getSize() + "]");
                    }
                } catch (UpdatedViewsQueueExceededException exception) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn(
                            "Updated views queue exceeded: " +
                                updatedViews.getICEfacesID());
                    }
                    _updatedViewsQueue.clear();
                    messageService.publish(
                        updatedViews.getICEfacesID(),
                        new Properties(),
                        UPDATED_VIEWS_QUEUE_EXCEEDED_MESSAGE_TYPE,
                        MessageServiceClient.RESPONSE_TOPIC_NAME);
                }
            }
        }
    }

    public void remove(final String iceFacesId) {
        if (iceFacesId != null && iceFacesId.trim().length() != 0) {
            synchronized (updatedViewsQueueMap) {
                if (updatedViewsQueueMap.containsKey(iceFacesId)) {
                    updatedViewsQueueMap.remove(iceFacesId);
                }
            }
        }
    }

    public void resetPurgeCounter(final String iceFacesId) {
        synchronized (updatedViewsQueueMap) {
            if (updatedViewsQueueMap.containsKey(iceFacesId)) {
                ((UpdatedViewsQueue)updatedViewsQueueMap.get(iceFacesId)).
                    resetPurgeCounter();
            }
        }
    }

    public void setPurgeMessageContents(final String purgeMessageContents) {
        this.purgeMessageContents = purgeMessageContents;
    }

    public void setUpdatedViewsQueueSize(final int updatedViewsQueueSize)
    throws IllegalArgumentException {
        if (updatedViewsQueueSize <= 0) {
            throw
                new IllegalArgumentException(
                    "illegal updated views queue size: " +
                        updatedViewsQueueSize);
        }
        this.updatedViewsQueueSize = updatedViewsQueueSize;
    }

    public void setUpdatedViewsQueueThreshold(
        final double updateViewsQueueThreshold)
    throws IllegalArgumentException {
        if (updateViewsQueueThreshold < 0.0 ||
            updateViewsQueueThreshold > 1.0) {

            throw
                new IllegalArgumentException(
                    "illegal updated views queue threshold: " +
                        updateViewsQueueThreshold);
        }
        this.updatedViewsQueueThreshold = updateViewsQueueThreshold;
    }

    void publishPurgeMap(final String iceFacesId) {
        StringBuffer _purgeMessage = new StringBuffer();
        synchronized (purgeMap) {
            if (purgeMessageContents.equalsIgnoreCase("all")) {
                Iterator _iceFacesIds = purgeMap.keySet().iterator();
                while (_iceFacesIds.hasNext()) {
                    String _iceFacesId = (String)_iceFacesIds.next();
                    _purgeMessage.append(_iceFacesId);
                    _purgeMessage.append(";");
                    _purgeMessage.append(purgeMap.get(_iceFacesId));
                    _purgeMessage.append("\r\n");
                }
            } else if (
                purgeMessageContents.equalsIgnoreCase("session") ||
                purgeMessageContents.equalsIgnoreCase("single")) {

                if (purgeMap.containsKey(iceFacesId)) {
                    _purgeMessage.append(iceFacesId);
                    _purgeMessage.append(";");
                    _purgeMessage.append(purgeMap.get(iceFacesId));
                    _purgeMessage.append("\r\n");
                }
            }
        }
        if (_purgeMessage.length() != 0) {
            messageService.publish(
                _purgeMessage.toString(),
                null,
                PURGE_MESSAGE_TYPE,
                MessageServiceClient.RESPONSE_TOPIC_NAME);
        }
    }

    void purged(final String iceFacesId, long sequenceNumber) {
        if (iceFacesId != null && iceFacesId.trim().length() != 0 &&
            sequenceNumber > 0) {

            synchronized (purgeMap) {
                if (!purgeMap.containsKey(iceFacesId) ||
                    sequenceNumber >
                        ((Long)purgeMap.get(iceFacesId)).longValue()) {
                    
                    purgeMap.put(iceFacesId, new Long(sequenceNumber));
                }
            }
        }
    }
}
