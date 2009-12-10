package org.icepush;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class PushContext {
    private static final ThreadLocal CurrentBrowserID = new ThreadLocal();
    private static final String BrowserIDCookieName = "ice.push.browser";
    private static final int GroupScanningTimeResolution = 3000;//ms
    private int browserCounter = 0;
    private int subCounter = 0;
    private Observable outboundNotifications;
    private Map groups = new HashMap();
    private long groupTimeout;

    public PushContext(Observable outboundNotifications, Observable inboundNotifications, Configuration configuration, ServletContext context) {
        this.outboundNotifications = outboundNotifications;
        this.groupTimeout = configuration.getAttributeAsLong("groupTimeout", 2 * 60 * 1000);
        context.setAttribute(PushContext.class.getName(), this);
        inboundNotifications.addObserver(new Observer() {
            private long lastScan = System.currentTimeMillis();

            public void update(Observable observable, Object o) {
                long now = System.currentTimeMillis();
                //avoid to scan/touch the groups on each notification
                if (lastScan + GroupScanningTimeResolution < now) {
                    lastScan = now;
                    List pushIDs = (List) o;
                    Iterator i = new ArrayList(groups.values()).iterator();
                    while (i.hasNext()) {
                        Group group = (Group) i.next();
                        group.touchIfMatching(pushIDs);
                        group.discardIfExpired();
                    }
                }
            }
        });
    }

    public synchronized String createPushId(HttpServletRequest request, HttpServletResponse response) {
        String browserID = getBrowserIDFromCookie(request);
        if (browserID == null) {
            if (CurrentBrowserID.get() == null) {
                browserID = generateBrowserID();
                CurrentBrowserID.set(browserID);
                response.addCookie(new Cookie(BrowserIDCookieName, browserID));
            } else {
                browserID = (String) CurrentBrowserID.get();
            }
        }

        return browserID + ":" + generateSubID();
    }

    public void push(String targetName) {
        Object o = groups.get(targetName);
        if (o == null) {
            outboundNotifications.notifyObservers(new String[]{targetName});
        } else {
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
    }

    public void removeGroupMember(String groupName, String pushId) {
        Object o = groups.get(groupName);
        if (o != null) {
            Group group = (Group) o;
            group.removeID(pushId);
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
            this.addID(firstPushId);
        }

        private void touchIfMatching(List pushIDs) {
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
                groups.remove(name);
            }
        }

        private void removeID(String id) {
            pushIDList.remove(id);
            if (pushIDList.isEmpty()) {
                groups.remove(name);
            }
        }

        private void addID(String id) {
            pushIDList.add(id);
        }

        private String[] getPushIDs() {
            return (String[]) pushIDList.toArray(new String[0]);
        }
    }
}
