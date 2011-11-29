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
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________ 
 * Contributors: ______________________
 */
package org.icefaces.renderkit;

import org.icefaces.ace.api.AjaxSource;
import org.icefaces.component.utils.Utils;
import org.icefaces.ace.JSONBuilder;


import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
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
  //          req.append("ice.ace.AjaxRequest(");

		    // node
	//	    req.append("{node:this");

            //source
   //         req.append("{source:").append("'").append(clientId).append("'");
            List<ClientBehaviorContext.Parameter> params = Collections.emptyList();
     //       req.append(",behaviors:{");
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

    //            req.append(domEvent + ":");

    //            req.append("function() {");
                for(Iterator<ClientBehavior> behaviorIter = behaviorEvents.get(event).iterator(); behaviorIter.hasNext();) {
                    ClientBehavior behavior = behaviorIter.next();
                    ClientBehaviorContext cbc = ClientBehaviorContext.createClientBehaviorContext(context, (UIComponent) component, event, clientId, params);
                    String script = behavior.getScript(cbc);    //could be null if disabled

                    if(script != null) {
      //                  System.out.println(" adding script piece="+script);
                        req.append(script);
                    }
                }
            //    req.append("}");

                if(eventIterator.hasNext()) {
                    req.append(",");
                }
            }

        //    req.append("}");
     //       System.out.println("creating ajax script of "+req.toString());
            return req.toString();


    }

    protected String addSubmitParam(String parent, String name, String value) {
        StringBuilder builder = new StringBuilder();

        builder.append(".addSubmitParam('")
                    .append(parent).append("','")
                    .append(name)
                    .append("','")
                    .append(value)
                    .append("')");

        return builder.toString();
    }
	
	protected String escapeText(String value) {
		return value == null ? "" : value.replaceAll("'", "\\\\'");
	}



    protected String encodeClientBehaviorEvents(FacesContext context, ClientBehaviorHolder component){
    //ClientBehaviors
  //      System.out.println("encode CBH events for component="+component.getClass().getName()+" has size=" +
  //             component.getEventNames().size());
        Map<String,List<ClientBehavior>> behaviorEvents = component.getClientBehaviors();
        if (behaviorEvents.isEmpty()){
            return null;
        }
            String clientId = ((UIComponent) component).getClientId(context);

            StringBuilder req = new StringBuilder();
            req.append("mobi.submit = {");


            //make function
            List<ClientBehaviorContext.Parameter> params = Collections.emptyList();
    //        req.append(",behaviors:{");
     //       System.out.println(" size of params list = "+params.size());
            for(Iterator<String> eventIterator = behaviorEvents.keySet().iterator(); eventIterator.hasNext();) {

                String event = eventIterator.next();
	             // event
		         req.append(event).append(":");

                String domEvent = event;
                if(event.equalsIgnoreCase("valueChange"))       //editable value holders
                    domEvent = "change";
                else if(event.equalsIgnoreCase("action"))       //commands
                    domEvent = "click";

    //            req.append(domEvent + ":");
                req.append("function(clientId){");
       //         req.append("function() {");
                for(Iterator<ClientBehavior> behaviorIter = behaviorEvents.get(event).iterator(); behaviorIter.hasNext();) {
                    ClientBehavior behavior = behaviorIter.next();
                    ClientBehaviorContext cbc = ClientBehaviorContext.createClientBehaviorContext(context, (UIComponent) component, event, clientId, params);
                    String script = behavior.getScript(cbc);    //could be null if disabled

                    if(script != null) {
                 //       System.out.println(" adding script piece="+script);
                        req.append(script);
                    }
                }
            //    req.append("}");

                if(eventIterator.hasNext()) {
                    req.append(",");
                }
            }

            req.append("}};");
       //     System.out.println("creating script of ="+req.toString());
            return req.toString();


    }

    /**
     * Non-obstrusive way to apply client behaviors.
     * Behaviors are rendered as options to the client side widget and applied by widget to necessary dom element
     */
     protected void encodeClientBehaviors(FacesContext context, ClientBehaviorHolder component, String eventDef) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
   //     System.out.println("encodeClientBehaviors");
        //ClientBehaviors
        Map<String,List<ClientBehavior>> behaviorEvents = component.getClientBehaviors();
        if(!behaviorEvents.isEmpty()) {
            String clientId = ((UIComponent) component).getClientId(context);
            List<ClientBehaviorContext.Parameter> params = Collections.emptyList();

            writer.write(",behaviors:{");

            for(Iterator<String> eventIterator = behaviorEvents.keySet().iterator(); eventIterator.hasNext();) {
                String event = eventIterator.next();
                String domEvent = event;

                if (null!=event){
                    domEvent=eventDef;
                }
                if(event.equalsIgnoreCase("valueChange"))       //editable value holders
                    domEvent = "change";
                else if(event.equalsIgnoreCase("action"))       //commands
                    domEvent = "click";

                writer.write(domEvent + ":");

                writer.write("function() {");
                for(Iterator<ClientBehavior> behaviorIter = behaviorEvents.get(event).iterator(); behaviorIter.hasNext();) {
                    ClientBehavior behavior = behaviorIter.next();
                    ClientBehaviorContext cbc = ClientBehaviorContext.createClientBehaviorContext(context, (UIComponent) component, event, clientId, params);
                    String script = behavior.getScript(cbc);    //could be null if disabled

                    if(script != null) {
                        writer.write(script);
                    }
                }
                writer.write("}");

                if(eventIterator.hasNext()) {
                    writer.write(",");
                }
            }

            writer.write("}");
        }
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