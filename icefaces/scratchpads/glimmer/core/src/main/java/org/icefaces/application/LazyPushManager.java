package org.icefaces.application;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class LazyPushManager {
    public LazyPushManager(HttpSession session) {
        session.setAttribute(LazyPushManager.class.getName(), this);
    }

    private int noOfRegistrations = 0;
    private HashMap individualyRegisteredViews = new HashMap();

    public boolean enablePush(String viewID) {
        if (noOfRegistrations > 0) {
            return true;
        }

        Integer no = (Integer) individualyRegisteredViews.get(viewID);
        return no != null && no > 0;
    }

    public void enablePushForView(String viewID) {
        Integer no = (Integer) individualyRegisteredViews.get(viewID);
        if (no == null) {
            individualyRegisteredViews.put(viewID, 1);
        } else {
            individualyRegisteredViews.put(viewID, ++no);
        }
    }

    public void disablePushForView(String viewID) {
        Integer no = (Integer) individualyRegisteredViews.get(viewID);
        if (no != null) {
            --no;

            if (no > 0) {
                individualyRegisteredViews.put(viewID, no);
            } else {
                individualyRegisteredViews.remove(viewID);
            }
        }
    }

    public void enablePushForSessionViews() {
        noOfRegistrations++;
    }

    public void disablePushForSessionViews() {
        noOfRegistrations--;
    }

    public static LazyPushManager lookup(FacesContext context) {
        Map sessionMap = context.getExternalContext().getSessionMap();
        return (LazyPushManager) sessionMap.get(LazyPushManager.class.getName());
    }
}
