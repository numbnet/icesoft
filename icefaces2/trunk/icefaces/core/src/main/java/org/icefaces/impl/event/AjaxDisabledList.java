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

import org.icefaces.util.EnvUtils;

import javax.faces.component.UIInput;
import javax.faces.component.UICommand;
import javax.faces.component.UIForm;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;

import java.util.Map;
import java.util.List;
import java.util.Iterator;

public class AjaxDisabledList implements SystemEventListener {
    public static String DISABLED_LIST = "org.icefaces.ajaxdisabledlist";

    public AjaxDisabledList() {
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!EnvUtils.isICEfacesView(context)) {
            return;
        }
        UIComponentBase component = (UIComponentBase) 
                ((PostAddToViewEvent) event).getComponent();

        Map behaviors = component.getClientBehaviors(); 
        if (null == behaviors)  {
            return;
        }
        boolean isDisabled = false;
        Iterator theBehaviors = behaviors.values().iterator();
        while (theBehaviors.hasNext())  {
        	ClientBehavior behavior =   (ClientBehavior)((List) theBehaviors.next()).get(0);
        	if(!(behavior instanceof AjaxBehavior)) continue;
            if (((AjaxBehavior)behavior).isDisabled())  {
                isDisabled = true;
                break;
            }
        }
        if (isDisabled)  {
            UIForm theForm = getContainingForm(component);
            if (null != theForm)  {
                String disabledList = (String) theForm.getAttributes()
                    .get(DISABLED_LIST);
                if (null == disabledList)  {
                    //ensure that the final string contains spaces around
                    //each id for accurate JavaScript test with indexOf
                    disabledList = " ";
                }
                disabledList = disabledList + component.getClientId() + " ";
                theForm.getAttributes().put(DISABLED_LIST, disabledList);
            }
        }
    }

    public boolean isListenerForSource(Object source) {
        return ( (source instanceof UIInput) || (source instanceof UICommand) );
    }

    public static UIForm getContainingForm(UIComponent component)  {
        UIComponent parent = component.getParent();
        while ( (!(parent instanceof UIForm)) &&
                (!(parent instanceof UIViewRoot)) )  {
            parent = parent.getParent();
        }
        if (parent instanceof UIForm)  {
            return (UIForm) parent;
        }
        return null;
    }

}

