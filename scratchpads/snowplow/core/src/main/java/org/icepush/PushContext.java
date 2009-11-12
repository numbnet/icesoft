package org.icepush;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class PushContext {
    private static final ThreadLocal CurrentBrowserID = new ThreadLocal();
    private static final String BrowserIDCookieName = "ice.push.browser";
    private int browserCounter = 0;
    private int pushCounter = 0;
    private Observable notificationObservable;
    private Map groups = new HashMap();

    public PushContext(Observable notificationObservable, ServletContext context) {
        this.notificationObservable = notificationObservable;
        context.setAttribute(PushContext.class.getName(), this);
    }

    public synchronized String createPushId(HttpServletRequest request, HttpServletResponse response) {
        String browserID = getBrowserIDFromCookie(request);
        if (browserID == null && CurrentBrowserID.get() == null) {
            browserID = generateBrowserID();
            CurrentBrowserID.set(browserID);
            response.addCookie(new Cookie(BrowserIDCookieName, browserID));
        }

        return generatePushID();
    }

    public void push(String targetName) {
        Object o = groups.get(targetName);
        if (o == null) {
            notificationObservable.notifyObservers(new String[]{targetName});
        } else {
            notificationObservable.notifyObservers(((ArrayList) o).toArray(new String[0]));
        }
    }

    public void addGroupMember(String groupName, String pushId) {
        Object o = groups.get(groupName);
        if (o == null) {
            ArrayList pushIDList = new ArrayList();
            pushIDList.add(pushId);
            groups.put(groupName, pushIDList);
        } else {
            ArrayList pushIDList = (ArrayList) o;
            pushIDList.add(pushId);
        }
    }

    public void removeGroupMember(String groupName, String pushId) {
        Object o = groups.get(groupName);
        if (o != null) {
            ArrayList pushIDList = (ArrayList) o;
            pushIDList.remove(pushId);
            if (pushIDList.isEmpty()) {
                groups.remove(groupName);
            }
        }
    }

    public static synchronized PushContext getInstance(ServletContext context) {
        return (PushContext) context.getAttribute(PushContext.class.getName());
    }


    private static String getBrowserIDFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (BrowserIDCookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    private synchronized String generateBrowserID() {
        //todo: find better algorithm
        return (System.currentTimeMillis() + "." + (++browserCounter));
    }

    private synchronized String generatePushID() {
        //todo: find better algorithm
        return Integer.toHexString((++pushCounter) + hashCode());
    }
}
