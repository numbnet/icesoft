package org.icepush;

import javax.servlet.http.HttpServletRequest;

public class PushContext {
    private String browserId;

    public static synchronized PushContext getInstance(HttpServletRequest request) {
        //the returned PushContext must be aware of the BROWSERID cookie and
        //must have a reference to the application scope notification service
        //request.getSession().getServletContext().getAttribute(NOTIFY_SERVICE);
        return null;
    }

    private PushContext(String browserId) {
        this.browserId = browserId;
    }

    public String createPushId(String browserId) {
        return "next pushid";
    }

    public void notify(String targetName) {
    }

    public void addGroupMember(String groupName, String pushId) {
    }

    public void removeGroupMember(String groupName, String pushId) {
    }
}
