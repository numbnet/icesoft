package org.icepush.integration.spring.samples.dice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icepush.PushContext;

public class PushRequestManager {
    public static final String BASE_GROUP_NAME = "roller";

    private String currentPushId = null;
    private String groupName;
    private PushContext pushContext = null;

    public PushRequestManager() {
    }

    public String getCurrentPushId() {
        return currentPushId;
    }

    public PushContext getPushContext() {
        return pushContext;
    }

    public String getGroupName() {
        return groupName;
    }

    public boolean isInitialized() {
        return ((pushContext != null) && (currentPushId != null)); 
    }

    public void intializePushContext(HttpServletRequest request,
                                     HttpServletResponse response)
                                 throws IllegalArgumentException {
        if (request == null) {
            throw new IllegalArgumentException("HttpServletRequest is null");
        }

        if (response == null) {
            throw new IllegalArgumentException("HttpServletResponse is null");
        }

        // Get an instance of the push context from the current request
        pushContext = PushContext.getInstance(request.getSession().getServletContext());
        // Generate a push ID for our session
        currentPushId = pushContext.createPushId(request, response);

        // Generate a unique group name for our session
        groupName = PushRequestManager.BASE_GROUP_NAME + request.getSession().getId();
        // Add ourselves to a push group
        pushContext.addGroupMember(groupName, currentPushId);
    }

    public void requestPush() {
        pushContext.push(groupName);
    }
}
