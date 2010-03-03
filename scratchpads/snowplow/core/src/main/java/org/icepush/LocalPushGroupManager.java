/*
 * Version: MPL 1.1
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package org.icepush;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

public class LocalPushGroupManager
implements PushGroupManager {
    private static final Logger LOGGER = Logger.getLogger(LocalPushGroupManager.class.getName());

    private static final int GROUP_SCANNING_TIME_RESOLUTION = 3000; // ms

    private final Map<String, Group> groupMap = new HashMap<String, Group>();

    private Set registeredPushIDs = new VetoedSet();

    private final long groupTimeout;
    private final Observable outboundNotifier;

    public LocalPushGroupManager(
        final Observable outboundNotifier, final Observable inboundNotifier, final DefaultConfiguration configuration,
        final ServletContext servletContext) {

        this.outboundNotifier = outboundNotifier;
        this.groupTimeout = configuration.getAttributeAsLong("groupTimeout", 2 * 60 * 1000);
        inboundNotifier.addObserver(
            new Observer() {
                private long lastScan = System.currentTimeMillis();
                private Set pushIDs = new HashSet();

                public void update(final Observable observable, final Object object) {
                    long now = System.currentTimeMillis();
                    //accumulate pushIDs
                    pushIDs.addAll((List)object);
                    //avoid to scan/touch the groups on each notification
                    if (lastScan + GROUP_SCANNING_TIME_RESOLUTION < now) {
                        try {
                            for (Group group : groupMap.values()) {
                                group.touchIfMatching(pushIDs);
                                group.discardIfExpired();
                            }
                        } finally {
                            lastScan = now;
                            pushIDs = new HashSet();
                        }
                    }
                }
            });
    }

    public void addMember(final String groupName, final String pushId) {
        if (groupMap.containsKey(groupName)) {
            groupMap.get(groupName).addPushID(pushId);
        } else {
            groupMap.put(groupName, new Group(groupName, pushId));
        }
        registeredPushIDs.add(pushId);
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "Added pushId '" + pushId + "' to group '" + groupName + "'.");
        }
    }

    public void push(final String targetName) {
        if (groupMap.containsKey(targetName)) {
            // targetName is a groupName
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST, "Push notification triggered for '" + targetName + "' group.");
            }
            outboundNotifier.notifyObservers(groupMap.get(targetName).getPushIDs());
        } else {
            // targetName is a pushId
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST, "Push notification triggered for '" + targetName + "' pushId.");
            }
            if (registeredPushIDs.contains(targetName)) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "'" + targetName + "' pushId does not belong to a group.");
                }
            }
            outboundNotifier.notifyObservers(new String[] {targetName});
        }
    }

    public void removeMember(final String groupName, final String pushId) {
        if (groupMap.containsKey(groupName)) {
            groupMap.get(groupName).removePushID(pushId);
            registeredPushIDs.remove(pushId);
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST, "Removed pushId '" + pushId + "' from group '" + groupName + "'.");
            }
        }
    }

    public void shutdown() {
        // Do nothing.
    }

    private class Group {
        private final Set<String> pushIdList = new HashSet<String>();

        private final String name;

        private long lastAccess = System.currentTimeMillis();

        private Group(final String name, final String firstPushId) {
            this.name = name;
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST, "'" + this.name + "' push group created.");
            }
            addPushID(firstPushId);
        }

        private void addPushID(final String pushId) {
            pushIdList.add(pushId);
        }

        private void discardIfExpired() {
            //expire group
            if (lastAccess + groupTimeout < System.currentTimeMillis()) {
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, "'" + name + "' push group expired.");
                }
                remove();
            }
        }

        private String[] getPushIDs() {
            return pushIdList.toArray(new String[pushIdList.size()]);
        }

        private void remove() {
            groupMap.remove(name);
            registeredPushIDs.removeAll(pushIdList);
        }

        private void removePushID(final String pushId) {
            pushIdList.remove(pushId);
            if (pushIdList.isEmpty()) {
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(
                        Level.FINEST, "Disposed '" + name + "' push group since it no longer contains any pushIds.");
                }
                remove();
            }
        }

        private void touchIfMatching(final Collection pushIds) {
            Iterator i = pushIds.iterator();
            while (i.hasNext()) {
                String pushId = (String)i.next();
                if (pushIdList.contains(pushId)) {
                    lastAccess = System.currentTimeMillis();
                    //no need to touch again
                    //return right away without checking the expiration
                    return;
                }
            }
        }
    }
}
