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

package org.icefaces.component.sliderentry;

import org.icefaces.component.utils.Utils;
import org.icefaces.impl.util.Util;

import java.io.IOException;

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

//Does JSF respect order?
//TODO make it so resources can be coalesce and served as whole 

public class SliderEntry extends SliderEntryBase{
    
    public void encodeBegin(FacesContext context) throws IOException {
        super.encodeBegin(context);
    }
    
    public void broadcast(FacesEvent event)
    throws AbortProcessingException {
        super.broadcast(event);

        //event was fired by me
        if (event != null) {
            
            //To keep it simple slider uses the broadcast to update value, so it doesn't
            //have to keep submitted value
            
            //1. update the value
            ValueExpression ve = getValueExpression("value");
            if (ve != null) {
                try {
                    setValue((Integer)((ValueChangeEvent)event).getNewValue());
                } catch (ELException ee) {
                    ee.printStackTrace();
                }
            } else {
                setValue((Integer)((ValueChangeEvent)event).getNewValue());
            }
            //invoke a valuechange listener if any
            MethodExpression method = getValueChangeListener();
            if (method != null) {
                method.invoke(getFacesContext().getELContext(), new Object[]{event});
            }
        }
    }
    
    public void queueEvent(FacesEvent event) {
        if (isImmediate()) {
            event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        }
        else {
            event.setPhaseId(PhaseId.INVOKE_APPLICATION);
        }
        super.queueEvent(event);
    } 

    
    public String getThumbUrl() {
    	String thumbUrl = super.getThumbUrl();
    	if (null == thumbUrl) {

            //ICE-6782: proper use of the ResourceHandler ensures resource URLs work in
            //both servlet and portlet environments.  This may be a good candidate for
            //a more generic utility method if we do this often in the ACE components.
            FacesContext fc = FacesContext.getCurrentInstance();
            Application app = fc.getApplication();
            ResourceHandler resourceHandler = app.getResourceHandler();
            Resource res = resourceHandler.createResource("assets/skins/sam/thumb-" + getAxis() + ".png","yui/3_3_0", "image/png");

            //The requestPath of the resource should be properly encoded for the
            //platform that it's running on.
            thumbUrl = res.getRequestPath();
    	}
    	return thumbUrl;
    }
    
    public boolean isSingleSubmit() {
        return Utils.superValueIfSet(this, getStateHelper(), PropertyKeys.singleSubmit.name(), super.isSingleSubmit(), Util.withinSingleSubmit(this));
    }
}
