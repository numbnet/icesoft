/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
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
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */
package org.icefaces.ace.component.ajax;

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.BehaviorConfig;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRuleset;

import org.icefaces.ace.api.IceClientBehaviorHolder;
import org.icefaces.ace.facelets.MethodRule;

import java.io.IOException;

public class AjaxBehaviorHandler extends AjaxBehaviorHandlerBase {
    private Class listenerEventClass;

    public AjaxBehaviorHandler(BehaviorConfig config) {
        super(config);
    }

    @Override
    protected MetaRuleset createMetaRuleset(Class type) {
        MetaRuleset metaRuleset = super.createMetaRuleset(type);

		metaRuleset.addRule(new MethodRule("listener", Void.TYPE,
            new Class[]{listenerEventClass}, "listenerNoArg"));
        
		return metaRuleset;
    }

    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        listenerEventClass = javax.faces.event.AjaxBehaviorEvent.class;
        if (parent instanceof IceClientBehaviorHolder) {
            IceClientBehaviorHolder aceParent = (IceClientBehaviorHolder) parent;
            String eventName = getEventName();
            if (eventName == null) {
                eventName = aceParent.getDefaultEventName();
            }
            if (eventName != null) {
                // Derive the event class name from the event name
                StringBuilder className = new StringBuilder("org.icefaces.ace.event.");
                int toUpperIndex = className.length();
                className.append(eventName).append("Event");
                className.setCharAt(toUpperIndex, Character.toUpperCase(className.charAt(toUpperIndex)));
                try {
                    Class clazz = Class.forName(className.toString());
                    if (javax.faces.event.AjaxBehaviorEvent.class.isAssignableFrom(clazz)) {
                        listenerEventClass = clazz;
                    }
                } catch(Exception e) {
                    // Silently eat this
                }
            }
        }
        super.apply(ctx, parent);
    }

}