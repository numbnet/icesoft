/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.impl.event;

import javax.faces.context.FacesContext;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UICommand;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlPanelGroup;

import org.icefaces.util.EnvUtils;

public class MainEventListener implements SystemEventListener  {
    private static String RENDER_STARTED = 
            MainEventListener.class.getName() + "-RENDER_STARTED"; 

    public MainEventListener()  {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (EnvUtils.isAutoId(facesContext))  {
            facesContext.getApplication()
                .subscribeToEvent(PostAddToViewEvent.class, this);
            facesContext.getApplication()
                .subscribeToEvent(PreRenderViewEvent.class, this);
        }
        AjaxDisabledList disabledList = new AjaxDisabledList();
        facesContext.getApplication()
            .subscribeToEvent(PostAddToViewEvent.class, disabledList);
    }

    public void processEvent(SystemEvent event)  {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!EnvUtils.isICEfacesView(facesContext)) {
            return;
        }
        if (event instanceof PreRenderViewEvent)  {
            facesContext.getAttributes().put(RENDER_STARTED, RENDER_STARTED);
            return;
        }
        if (null != facesContext.getAttributes().get(RENDER_STARTED))  {
            //do not modify component IDs after rendering has begun
            return;
        }
        UIComponent component = ((PostAddToViewEvent)event).getComponent();
        String id = component.getId();
        if (null == id)  {
            return;
        }
        if (id.startsWith("j_id") && (shouldModifyId(component)))  {
            id = "_" + id.substring(4);
            component.setId(id);
            component.getAttributes().put("id", id);
        }
    }

    public boolean isListenerForSource(Object source)  {
        if (source instanceof UIViewRoot)  {
            return true;
        }
        return shouldModifyId(source);
    }

    public boolean shouldModifyId(Object source)  {
        //Existing ice: components already output ids
        if (source.getClass().getName().startsWith("com.icesoft"))  {
            return false;
        }
        boolean classCheck = (  (
            (source instanceof UIOutput) || 
            (source instanceof HtmlDataTable) || 
            (source instanceof HtmlPanelGroup) || 
            (source instanceof UICommand) ) && (!UIOutput.class.equals(source.getClass())) );
        return classCheck;
    }
}