/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package org.icefaces.push;

import org.icepush.PushContext;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.io.Serializable;

public class SessionViewManager implements SessionRenderer, Serializable {
    private transient PushContext pushContext;
    private String groupName;
    private HashSet viewIDs = new HashSet();
    private HashSet groups = new HashSet();

    public SessionViewManager(PushContext pushContext, HttpSession session) {
        this.pushContext = pushContext;
        this.groupName = session.getId();
        session.setAttribute(SessionViewManager.class.getName(), this);
        session.setAttribute(SessionRenderer.class.getName(), this);
    }

    public String addView(String id) {
        viewIDs.add(id);
        getPushContext().addGroupMember(groupName, id);

        Iterator i = groups.iterator();
        while (i.hasNext()) {
            getPushContext().addGroupMember((String) i.next(), id);
        }
        return id;
    }

    public void removeView(String id) {
        viewIDs.remove(id);
        getPushContext().removeGroupMember(groupName, id);

        Iterator i = groups.iterator();
        while (i.hasNext()) {
            getPushContext().removeGroupMember((String) i.next(), id);
        }
    }

    public static SessionViewManager lookup(FacesContext context) {
        Map sessionMap = context.getExternalContext().getSessionMap();
        return (SessionViewManager) sessionMap.get(SessionViewManager.class.getName());
    }

    public void renderViews() {
        getPushContext().push(groupName);
    }

    public void addCurrentViewsToGroup(String groupName) {
        groups.add(groupName);
        Iterator i = viewIDs.iterator();
        while (i.hasNext()) {
            getPushContext().addGroupMember(groupName, (String) i.next());
        }
    }

    public void removeCurrentViewsFromGroup(String groupName) {
        groups.remove(groupName);
        Iterator i = viewIDs.iterator();
        while (i.hasNext()) {
            getPushContext().removeGroupMember(groupName, (String) i.next());
        }
    }
    
    private PushContext getPushContext()  {
        if (null == pushContext)  {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            pushContext = (PushContext) facesContext.getExternalContext()
                    .getApplicationMap().get(PushContext.class.getName());
        }
        return pushContext;
    }
}
