package org.icepush;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class PushContext {
    private int counter = 0;
    private Observable notificationObservable;
    private Map groups = new HashMap();

    public PushContext(Observable notificationObservable) {
        this.notificationObservable = notificationObservable;
    }

    public synchronized String createPushId() {
        return System.currentTimeMillis() + "." + counter++;
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

    public static synchronized PushContext getInstance(HttpServletRequest request) {
        return getInstance(request.getSession());
    }

    public static synchronized PushContext getInstance(HttpSession session) {
        //the returned PushContext must be aware of the BROWSERID cookie and
        //must have a reference to the application scope notification service
        //request.getSession().getServletContext().getAttribute(NOTIFY_SERVICE);
        return (PushContext) session.getAttribute(PushContext.class.getName());
    }
}
