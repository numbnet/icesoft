package org.icepush;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class PushContext {
    private int counter = 0;
    private Observable notificationObservable;
    private Map groups = new HashMap();

    public PushContext(Observable notificationObservable, ServletContext context) {
        this.notificationObservable = notificationObservable;
        context.setAttribute(PushContext.class.getName(), this);
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

    public static synchronized PushContext getInstance(ServletContext context) {
        return (PushContext) context.getAttribute(PushContext.class.getName());
    }
}
