package org.icepush;

import javax.servlet.http.HttpServletRequest;
import java.util.Observable;

public class PushContext {
    private int counter = 0;
    private Observable notificationObservable;

    public PushContext(Observable notificationObservable) {
        this.notificationObservable = notificationObservable;
    }

    public synchronized String createPushId() {
        return System.currentTimeMillis() + "." + counter++;
    }

    public void push(String targetName) {
        notificationObservable.notifyObservers(targetName);
    }

    public void addGroupMember(String groupName, String pushId) {
    }

    public void removeGroupMember(String groupName, String pushId) {
    }

    public static synchronized PushContext getInstance(HttpServletRequest request) {
        //the returned PushContext must be aware of the BROWSERID cookie and
        //must have a reference to the application scope notification service
        //request.getSession().getServletContext().getAttribute(NOTIFY_SERVICE);
        return (PushContext) request.getSession().getAttribute(PushContext.class.getName());
    }
}
