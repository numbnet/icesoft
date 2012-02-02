/*
 * Original Code developed and contributed by Prime Technology.
 * Subsequent Code Modifications Copyright 2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: (ICE-6978) Used JSONBuilder to add the functionality of escaping JS output.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 */
package org.icefaces.component.ajax;

import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.PhaseId;
import javax.faces.render.ClientBehaviorRenderer;
import javax.faces.render.FacesBehaviorRenderer;

import org.icefaces.mobi.utils.Utils;
import org.icefaces.ace.JSONBuilder;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.ace.api.IceClientBehaviorHolder;

//mandatory resource doesn't work with behaviors, so script for this is in component.js
@FacesBehaviorRenderer(rendererType="org.icefaces.component.AjaxBehaviorRenderer")
public class AjaxBehaviorRenderer extends ClientBehaviorRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component, ClientBehavior behavior) {
        AjaxBehavior ajaxBehavior = (AjaxBehavior) behavior;

        if(!ajaxBehavior.isDisabled()) {
            AjaxBehaviorEvent event = new AjaxBehaviorEvent(component, behavior);

            PhaseId phaseId = isImmediate(component, ajaxBehavior) ? PhaseId.APPLY_REQUEST_VALUES : PhaseId.INVOKE_APPLICATION;

            event.setPhaseId(phaseId);

            component.queueEvent(event);
        }
    }

    @Override
    public String getScript(ClientBehaviorContext behaviorContext, ClientBehavior behavior) {
        AjaxBehavior ajaxBehavior = (AjaxBehavior) behavior;
        if(ajaxBehavior.isDisabled()) {
            return null;
        }
        
        FacesContext fc = behaviorContext.getFacesContext();
        UIComponent component = behaviorContext.getComponent();
        String clientId = component.getClientId(fc);
        String source = behaviorContext.getSourceId();

        JSONBuilder jb = JSONBuilder.create();
        jb.beginFunction("mobi.AjaxRequest");
        //source
		jb.beginMap()
			.entry("source", source);
		
        //execute
		String execute = null;
        if (ajaxBehavior.getExecute() != null) {
			execute = ajaxBehavior.getExecute();
		} else {
			if (component instanceof IceClientBehaviorHolder) {
				execute = ((IceClientBehaviorHolder) component).getDefaultExecute(behaviorContext.getEventName());
			}
		}
		if (execute == null || "".equals(execute)) {
			jb.entry("execute", clientId);
		} else {
			jb.entry("execute", Utils.findClientIds(fc, component, execute));
		}

        //render
		String render = null;
        if (ajaxBehavior.getRender() != null) {
            render = ajaxBehavior.getRender();
        } else {
			if (component instanceof IceClientBehaviorHolder) {
				render = ((IceClientBehaviorHolder) component).getDefaultRender(behaviorContext.getEventName());
			}
			if (render == null || "".equals(render)) {
				render = "@all";
			}
		}
		jb.entry("render", Utils.findClientIds(fc, component, render));

        //behavior event
		jb.entry("event", behaviorContext.getEventName());

        //callbacks
        if(ajaxBehavior.getOnStart() != null)
            jb.entry("onstart", "function(xhr){" + ajaxBehavior.getOnStart() + ";}", true);
        if(ajaxBehavior.getOnError() != null)
            jb.entry("onerror", "function(xhr, status, error){" + ajaxBehavior.getOnError() + ";}", true);
        if(ajaxBehavior.getOnSuccess() != null)
            jb.entry("onsuccess", "function(data, status, xhr, args){" + ajaxBehavior.getOnSuccess() + ";}", true);
        if(ajaxBehavior.getOnComplete() != null)
            jb.entry("oncomplete", "function(xhr, status, args){" + ajaxBehavior.getOnComplete() + ";}", true);

        //params
        jb.entry("params", "arguments[1]", true);
		
		jb.endMap().endFunction();
  //      System.out.println("AjaxBehaviorRender creates:-"+jb.toString());
        return jb.toString();
    }

    private boolean isImmediate(UIComponent component, AjaxBehavior ajaxBehavior) {
        boolean immediate = false;

        if(ajaxBehavior.isImmediateSet()) {
            immediate = ajaxBehavior.isImmediate();
        } else if(component instanceof EditableValueHolder) {
            immediate = ((EditableValueHolder)component).isImmediate();
        } else if(component instanceof ActionSource) {
            immediate = ((ActionSource)component).isImmediate();
        }

        return immediate;
    }
}
