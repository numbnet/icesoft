package org.icefaces.push;

import org.icepush.PushContext;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class ViewNotificationManager implements SessionRenderer {
    private int index;
    private final PushContext pushContext;
    private String groupName;

    public ViewNotificationManager(PushContext pushContext, HttpSession session) {
        this.pushContext = pushContext;
        this.index = 0;
        this.groupName = session.getId();
        session.setAttribute(ViewNotificationManager.class.getName(), this);
        session.setAttribute(SessionRenderer.class.getName(), this);
    }

    public String assignViewIdentifier() {
        String id = generateID();
        pushContext.addGroupMember(groupName, id);
        return id;
    }

    public void disposeViewIdentifier(String id) {
        pushContext.removeGroupMember(groupName, id);
    }

    public static ViewNotificationManager lookup(FacesContext context) {
        Map sessionMap = context.getExternalContext().getSessionMap();
        return (ViewNotificationManager) sessionMap.get(ViewNotificationManager.class.getName());
    }

    private synchronized String generateID() {
        return Integer.toString(++index, 36);
    }

    public void renderViews() {
        pushContext.push(groupName);
    }
}
