/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icefaces.mobi.renderkit;




import javax.faces.component.UIComponent;

import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.*;
import javax.faces.context.ResponseWriter;

public class CoreRenderer extends Renderer {

    /**
     * this method created for mobi:inputText
     * @param context
     * @param component
     * @param inEvent
     * @return
     */
    protected String buildAjaxRequest(FacesContext context, ClientBehaviorHolder component, String inEvent) {
      //ClientBehaviors
 //       System.out.println("building Ajax Request");
        Map<String,List<ClientBehavior>> behaviorEvents = component.getClientBehaviors();
        if (behaviorEvents.isEmpty()){
     //       System.out.println("why is behaviorEvents list empty????");
            return null;
        }

        String clientId = ((UIComponent) component).getClientId(context);

        StringBuilder req = new StringBuilder();

        List<ClientBehaviorContext.Parameter> params = Collections.emptyList();

        for(Iterator<String> eventIterator = behaviorEvents.keySet().iterator(); eventIterator.hasNext();) {

                String event = eventIterator.next();

                String domEvent = event;
            if (null != inEvent) {
                    domEvent=inEvent;
            }
            else if(event.equalsIgnoreCase("valueChange"))       //editable value holders
                domEvent = "change";
            else if(event.equalsIgnoreCase("action"))       //commands
                domEvent = "click";
            for(Iterator<ClientBehavior> behaviorIter = behaviorEvents.get(event).iterator(); behaviorIter.hasNext();) {
                    ClientBehavior behavior = behaviorIter.next();
                    ClientBehaviorContext cbc = ClientBehaviorContext.createClientBehaviorContext(context, (UIComponent) component, event, clientId, params);
                    String script = behavior.getScript(cbc);    //could be null if disabled

                    if(script != null) {
                        req.append(script);
                    }
            }
            if(eventIterator.hasNext()) {
                req.append(",");
            }
        }
        return req.toString();

    }


    public boolean isValueEmpty(String value) {
		if (value == null || "".equals(value))
			return true;

		return false;
	}

	public boolean isValueBlank(String value) {
		if(value == null)
			return true;

		return value.trim().equals("");
	}


    /**
      * Non-obstrusive way to apply client behaviors.
      * Behaviors are rendered as options to the client side widget and applied by widget to necessary dom element
      */
      protected StringBuilder encodeClientBehaviors(FacesContext context, ClientBehaviorHolder component, String eventDef) throws IOException {
         ResponseWriter writer = context.getResponseWriter();
         StringBuilder sb = new StringBuilder(255);
         //ClientBehaviors
         Map<String,List<ClientBehavior>> behaviorEvents = component.getClientBehaviors();
         if(!behaviorEvents.isEmpty()) {
             String clientId = ((UIComponent) component).getClientId(context);
             List<ClientBehaviorContext.Parameter> params = Collections.emptyList();

             sb.append(",behaviors:{");

             for(Iterator<String> eventIterator = behaviorEvents.keySet().iterator(); eventIterator.hasNext();) {
                 String event = eventIterator.next();
                 String domEvent = event;
                 if (null==event){
                     event = eventDef;
                 }
                 if(event.equalsIgnoreCase("valueChange"))       //editable value holders
                     domEvent = "change";
                 else if(event.equalsIgnoreCase("action"))       //commands
                     domEvent = "click";

                 sb.append(domEvent + ":");
                 sb.append("function() {");
                 ClientBehaviorContext cbc = ClientBehaviorContext.createClientBehaviorContext(context, (UIComponent) component, event, clientId, params);
                 for(Iterator<ClientBehavior> behaviorIter = behaviorEvents.get(event).iterator(); behaviorIter.hasNext();) {
                     ClientBehavior behavior = behaviorIter.next();
                     String script = behavior.getScript(cbc);    //could be null if disabled
                     if(script != null) {
                         sb.append(script);
                     }
                 }
                 sb.append("}");

                 if(eventIterator.hasNext()) {
                     sb.append(",");
                 }
             }

             sb.append("}");
         }
          return sb;
     }


    protected void decodeBehaviors(FacesContext context, UIComponent component)  {
        if(!(component instanceof ClientBehaviorHolder)) {
            return;
        }
        Map<String, List<ClientBehavior>> behaviors = ((ClientBehaviorHolder) component).getClientBehaviors();
        if(behaviors.isEmpty()) {
            return;
        }
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String behaviorEvent = params.get("javax.faces.behavior.event");

        if(null != behaviorEvent) {
            List<ClientBehavior> behaviorsForEvent = behaviors.get(behaviorEvent);

            if(behaviors.size() > 0) {
               String behaviorSource = params.get("javax.faces.source");
               String clientId = component.getClientId();

               if(behaviorSource != null && behaviorSource.startsWith(clientId)) {
                   for (ClientBehavior behavior: behaviorsForEvent) {
                       behavior.decode(context, component);
                   }
               }
            }
        }
    }
}