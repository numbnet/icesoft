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

import java.util.logging.Logger;


import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;

import java.util.Iterator;

import com.sun.faces.facelets.tag.ui.UIDebug;

/* ICE-5717 the <ui:debug> tag was causing full page refreshes so wrap it with 
 *   a panelGroup in order to contain the updated*/

public class DebugTagListener implements SystemEventListener {
    private final static Logger log = Logger.getLogger(DebugTagListener.class.getName());
	
    public boolean isListenerForSource(Object source) {
		 return source.getClass().getName().equals("com.sun.faces.facelets.tag.ui.UIDebug");
	}

	public void processEvent(SystemEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!EnvUtils.isICEfacesView(context)) {
            return;
        }

        if (event instanceof PostAddToViewEvent){
            final UIDebug debugTag = (UIDebug) ((PostAddToViewEvent) event).getComponent();
            String debugId = debugTag.getId();
            UIComponent parent = (UIComponent)debugTag.getParent();
            //We can pretend the following hack is legitimate because the
            //UIDebug component should be transient and is not useful during
            //restore view
            if (context.getCurrentPhaseId().equals(PhaseId.RESTORE_VIEW))  {
                   parent.getChildren().remove(debugTag);
                   return;
            }

            if (parent instanceof javax.faces.component.html.HtmlPanelGroup)
               return; //do nothing as it's already contained in panelGroup
            else{
	           //find debug in the list of children to know where to re-insert
	           Iterator<UIComponent> kids = parent.getChildren().iterator();
	           int debugLocation = 0;
	           int counter = 0;
	           while (kids.hasNext()) {
	                if (kids.next().getId().equals(debugId)){
	                	debugLocation=counter;
	                }
	                counter++;
	           }
	           parent.getChildren().remove(debugTag);
               //create a panelGroup to enclose the debug tag
	           UIComponent enclosingPanel = new javax.faces.component.html.HtmlPanelGroup();
	           enclosingPanel.setId("debugPanel");
	           enclosingPanel.setInView(true);
	           enclosingPanel.setTransient(true);
	           //set parent of debug to this enclosingPanel 
	           enclosingPanel.getChildren().add(debugTag);	            
	           parent.getChildren().add(debugLocation,enclosingPanel);
            }

        }
	}
}
