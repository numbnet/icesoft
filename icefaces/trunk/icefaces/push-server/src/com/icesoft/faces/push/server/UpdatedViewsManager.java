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
package com.icesoft.faces.push.server;

import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.net.messaging.MessageServiceClient;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.icefaces.push.server.SessionManager;

public class UpdatedViewsManager
extends org.icefaces.push.server.UpdatedViewsManager
implements PurgeMessageHandler.Callback {
    private static final String PURGE_MESSAGE_TYPE = "Purge";

    private static final Log LOG =
        LogFactory.getLog(UpdatedViewsManager.class);

    private final Map purgeMap = new HashMap();

    private String purgeMessageContents;
    private double updatedViewsQueueThreshold;

    public UpdatedViewsManager(
        final Configuration configuration,
        final MessageService messageService,
        final SessionManager sessionManager) {

        super(configuration, messageService, sessionManager);
        setPurgeMessageContents(
            configuration.getAttribute(
                "purgeMessageContents", "all"));
        setUpdatedViewsQueueThreshold(
            configuration.getAttributeAsDouble(
                "updatedViewsQueueThreshold", 0.7d));
        this.messageService.setCallback(
            PurgeMessageHandler.Callback.class, this);
    }

    public String getPurgeMessageContents() {
        return purgeMessageContents;
    }

    public double getUpdatedViewsQueueThreshold() {
        return updatedViewsQueueThreshold;
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

    public void remove(final String iceFacesId) {
        if (iceFacesId != null && iceFacesId.trim().length() != 0) {
            synchronized (updatedViewsQueueMap) {
                synchronized (purgeMap) {
                    super.remove(iceFacesId);
                    if (purgeMap.containsKey(iceFacesId)) {
                        purgeMap.remove(iceFacesId);
                    }
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

    protected UpdatedViewsQueue newUpdatedViewsQueue(
        final String iceFacesId,
        final UpdatedViewsManager updatedViewsManager) {

        return new UpdatedViewsQueue(iceFacesId, updatedViewsManager);
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
                MessageServiceClient.PUSH_TOPIC_NAME);
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
