package org.icefaces.push;

import org.icepush.PushContext;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class SessionViewManager implements SessionRenderer {
    private final PushContext pushContext;
    private String groupName;
    private ArrayList viewIDs = new ArrayList();
    private ArrayList groups = new ArrayList();

    public SessionViewManager(PushContext pushContext, HttpSession session) {
        this.pushContext = pushContext;
        this.groupName = session.getId();
        session.setAttribute(SessionViewManager.class.getName(), this);
        session.setAttribute(SessionRenderer.class.getName(), this);
    }

    public String addView(String id) {
        viewIDs.add(id);
        pushContext.addGroupMember(groupName, id);

        Iterator i = groups.iterator();
        while (i.hasNext()) {
            pushContext.addGroupMember((String) i.next(), id);
        }
        return id;
    }

    public void removeView(String id) {
        viewIDs.remove(id);
        pushContext.removeGroupMember(groupName, id);

        Iterator i = groups.iterator();
        while (i.hasNext()) {
            pushContext.removeGroupMember((String) i.next(), id);
        }
    }

    public static SessionViewManager lookup(FacesContext context) {
        Map sessionMap = context.getExternalContext().getSessionMap();
        return (SessionViewManager) sessionMap.get(SessionViewManager.class.getName());
    }

    public void renderViews() {
        pushContext.push(groupName);
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
