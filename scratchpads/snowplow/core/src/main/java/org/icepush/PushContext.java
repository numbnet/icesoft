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

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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

public class PushContext {
    private static final Logger log = Logger.getLogger(PushContext.class.getName());
    private static final String BrowserIDCookieName = "ice.push.browser";
    private static final int GroupScanningTimeResolution = 3000;//ms
    private int browserCounter = 0;
    private int subCounter = 0;
    private Observable outboundNotifications;
    private Map groups = new HashMap();
    private Set registeredPushIDs = new VetoedSet();
    private long groupTimeout;

    public PushContext(Observable outboundNotifications, Observable inboundNotifications, Configuration configuration, ServletContext context) {
        this.outboundNotifications = outboundNotifications;
        this.groupTimeout = configuration.getAttributeAsLong("groupTimeout", 2 * 60 * 1000);
        context.setAttribute(PushContext.class.getName(), this);
        inboundNotifications.addObserver(new Observer() {
            private long lastScan = System.currentTimeMillis();
            private HashSet pushIDs = new HashSet();

            public void update(Observable observable, Object o) {
                long now = System.currentTimeMillis();
                //accumulate pushIDs
                pushIDs.addAll((List) o);
                //avoid to scan/touch the groups on each notification
                if (lastScan + GroupScanningTimeResolution < now) {
                    try {
                        Iterator i = new ArrayList(groups.values()).iterator();
                        while (i.hasNext()) {
                            Group group = (Group) i.next();
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

    public synchronized String createPushId(HttpServletRequest request, HttpServletResponse response) {
        String browserID = getBrowserIDFromCookie(request);
        if (browserID == null) {
            String currentBrowserID = (String)
                    request.getAttribute(BrowserIDCookieName);
            if (null == currentBrowserID) {
                browserID = generateBrowserID();
                response.addCookie(new Cookie(BrowserIDCookieName, browserID));
                request.setAttribute(BrowserIDCookieName, browserID);
            } else {
                browserID = currentBrowserID;
            }
        }

        String id = browserID + ":" + generateSubID();
        if (log.isLoggable(Level.FINEST)) {
            log.finest("Created new pushId '" + id + "'.");
        }
        return id;
    }

    public void push(String targetName) {
        Object o = groups.get(targetName);
        if (o == null) {
            if (log.isLoggable(Level.FINEST)) {
                log.finest("Push notification triggered for '" + targetName + "' pushId.");
            }
            if (!registeredPushIDs.contains(targetName)) {
                log.warning("'" + targetName + "' pushId does not belong to a group.");
            }
            outboundNotifications.notifyObservers(new String[]{targetName});
        } else {
            if (log.isLoggable(Level.FINEST)) {
                log.finest("Push notification triggered for '" + targetName + "' group.");
            }
            outboundNotifications.notifyObservers(((Group) o).getPushIDs());
        }
    }

    public void addGroupMember(String groupName, String pushId) {
        Object o = groups.get(groupName);
        if (o == null) {
            groups.put(groupName, new Group(groupName, pushId));
        } else {
            ((Group) o).addID(pushId);
        }
        registeredPushIDs.add(pushId);
        if (log.isLoggable(Level.FINEST)) {
            log.finest("Added pushId '" + pushId + "' to group '" + groupName + "'.");
        }
    }

    public void removeGroupMember(String groupName, String pushId) {
        Object o = groups.get(groupName);
        if (o != null) {
            Group group = (Group) o;
            group.removeID(pushId);
            registeredPushIDs.remove(pushId);
            if (log.isLoggable(Level.FINEST)) {
                log.finest("Added pushId '" + pushId + "' to group '" + groupName + "'.");
            }
        }
    }

    public static synchronized PushContext getInstance(ServletContext context) {
        return (PushContext) context.getAttribute(PushContext.class.getName());
    }

    private static String getBrowserIDFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (BrowserIDCookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    private synchronized String generateBrowserID() {
        return Long.toString(++browserCounter, 36) + Long.toString(System.currentTimeMillis(), 36);
    }

    private synchronized String generateSubID() {
        return Integer.toString((++subCounter) + (hashCode() / 10000), 36);
    }

    private class Group {
        private String name;
        private long lastAccess = System.currentTimeMillis();
        private HashSet pushIDList = new HashSet();

        private Group(String name, String firstPushId) {
            this.name = name;
            if (log.isLoggable(Level.FINEST)) {
                log.finest("'" + name + "' push group created.");
            }
            this.addID(firstPushId);
        }

        private void touchIfMatching(Collection pushIDs) {
            Iterator i = pushIDs.iterator();
            while (i.hasNext()) {
                String pushID = (String) i.next();
                if (pushIDList.contains(pushID)) {
                    lastAccess = System.currentTimeMillis();
                    //no need to touch again
                    //return right away without checking the expiration
                    return;
                }
            }
        }

        private void discardIfExpired() {
            //expire group
            if (lastAccess + groupTimeout < System.currentTimeMillis()) {
                if (log.isLoggable(Level.FINEST)) {
                    log.finest("'" + name + "' push group expired.");
                }
                remove();
            }
        }

        private void removeID(String id) {
            pushIDList.remove(id);
            if (pushIDList.isEmpty()) {
                if (log.isLoggable(Level.FINEST)) {
                    log.finest("Disposed '" + name + "' push group since it no longer contains any pushIds.");
                }
                remove();
            }
        }

        private void remove() {
            groups.remove(name);
            registeredPushIDs.removeAll(pushIDList);
        }

        private void addID(String id) {
            pushIDList.add(id);
        }

        private String[] getPushIDs() {
            return (String[]) pushIDList.toArray(new String[0]);
        }
    }
}
