package org.icefaces.push;

import org.icepush.PushContext;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ViewNotificationManager implements SessionRenderer {
    private int index;
    private final PushContext pushContext;
    private String groupName;
    private ArrayList viewIDs = new ArrayList();
    private ArrayList groups = new ArrayList();

    public ViewNotificationManager(PushContext pushContext, HttpSession session) {
        this.pushContext = pushContext;
        this.index = 0;
        this.groupName = session.getId();
        session.setAttribute(ViewNotificationManager.class.getName(), this);
        session.setAttribute(SessionRenderer.class.getName(), this);
    }

    public String assignViewIdentifier() {
        String id = generateID();
        viewIDs.add(id);
        pushContext.addGroupMember(groupName, id);

        Iterator i = groups.iterator();
        while (i.hasNext()) {
            pushContext.addGroupMember((String) i.next(), id);
        }
        return id;
    }

    public void disposeViewIdentifier(String id) {
        viewIDs.remove(id);
        pushContext.removeGroupMember(groupName, id);

        Iterator i = groups.iterator();
        while (i.hasNext()) {
            pushContext.removeGroupMember((String) i.next(), id);
        }
    }

    public static ViewNotificationManager lookup(FacesContext context) {
        Map sessionMap = context.getExternalContext().getSessionMap();
        return (ViewNotificationManager) sessionMap.get(ViewNotificationManager.class.getName());
    }

    public void renderViews() {
        pushContext.push(groupName);
    }

    private synchronized String generateID() {
        return Integer.toString(++index, 36);
    }

    public void addCurrentViewsToGroup(String groupName) {
        groups.add(groupName);
        Iterator i = viewIDs.iterator();
        while (i.hasNext()) {
            pushContext.addGroupMember(groupName, (String) i.next());
        }
    }

    public void removeCurrentViewsFromGroup(String groupName) {
        groups.remove(groupName);
        Iterator i = viewIDs.iterator();
        while (i.hasNext()) {
            pushContext.removeGroupMember(groupName, (String) i.next());
        }
    }
}
