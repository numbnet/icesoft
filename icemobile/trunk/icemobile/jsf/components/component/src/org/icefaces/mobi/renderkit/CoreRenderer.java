/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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


import static org.icemobile.util.HTML.CLASS_ATTR;

import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.JSONBuilder;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;

import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.lang.StringBuilder;
import java.util.*;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.faces.context.ResponseWriter;

public class CoreRenderer extends MobileBaseRenderer {
    private static final Logger logger = Logger.getLogger(CoreRenderer.class.getName());
    /**
     * this method created for mobi:inputText
     * @param context
     * @param component
     * @param inEvent
     * @return
     */
    protected String buildAjaxRequest(FacesContext context, ClientBehaviorHolder component, String inEvent) {
        Map<String,List<ClientBehavior>> behaviorEvents = component.getClientBehaviors();
        if (behaviorEvents.isEmpty()){
            return null;
        }

        String clientId = ((UIComponent) component).getClientId(context);

        StringBuilder req = new StringBuilder();

        List<ClientBehaviorContext.Parameter> params = Collections.emptyList();

        for(Iterator<String> eventIterator = behaviorEvents.keySet().iterator(); eventIterator.hasNext();) {
            String event = eventIterator.next();
       //     logger.info("eventIterator returning="+event);
            String domEvent = event;
            if (null != inEvent) {
                domEvent = inEvent;
   //             logger.info("passed in event="+event);
            }
            domEvent = getDomEvent(event);
      //      logger.info("getDomEvent returns event="+domEvent);
            if (behaviorEvents.get(event)==null){
                logger.warning(" NO behavior for event="+event+" component="+((UIComponent) component).getClientId());
                return null;
            }  //don't do anything with domEvent yet as have to use the one the behavior is registered with.
       //     logger.info("before interation event="+event);
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
    //    logger.info(" request string = "+req.toString());
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
      * Non-obstrusive way to apply client behaviors.  Brought over from implementation of ace components for ace ajax.
      * will be replaced in 1.4 Beta to reflect support for both mobi:transition and mobi:ajax behaviors
      * Behaviors are rendered as options to the client side widget and applied by widget to necessary dom element
      */
   /* protected StringBuilder encodeClientBehaviors(FacesContext context, ClientBehaviorHolder component, String eventDef) throws IOException {
       StringBuilder sb = new StringBuilder(255);
         //ClientBehaviors
       Map<String,List<ClientBehavior>> eventBehaviors = component.getClientBehaviors();
       if(!eventBehaviors.isEmpty()) {
           String clientId = ((UIComponent) component).getClientId(context);
           List<ClientBehaviorContext.Parameter> params = Collections.emptyList();

           sb.append(",behaviors:{");

           for(Iterator<String> eventIterator = eventBehaviors.keySet().iterator(); eventIterator.hasNext();) {
               String event = eventIterator.next();
               if (null==event){
                   event = eventDef;
               }
               String domEvent = getDomEvent(event);
               sb.append(domEvent + ":");
               sb.append("function() {");
               ClientBehaviorContext cbContext = ClientBehaviorContext.createClientBehaviorContext(context, (UIComponent) component, event, clientId, params);
               for(Iterator<ClientBehavior> behaviorIter = eventBehaviors.get(event).iterator(); behaviorIter.hasNext();) {
                   ClientBehavior behavior = behaviorIter.next();
                   String script = behavior.getScript(cbContext);
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
    }   */
     protected void encodeClientBehaviors(FacesContext context, ClientBehaviorHolder component, JSONBuilder jb) throws IOException {
        Map<String,List<ClientBehavior>> behaviorEvents = component.getClientBehaviors();
        if (behaviorEvents.isEmpty()) return;

        String clientId = ((UIComponent) component).getClientId(context);
        List<ClientBehaviorContext.Parameter> params = Collections.emptyList();

        jb.beginMap("behaviors");

		List<String> sortedBehaviourList = new ArrayList<String>(behaviorEvents.keySet());
		Collections.sort(sortedBehaviourList);

        for(Iterator<String> eventIterator = sortedBehaviourList.iterator(); eventIterator.hasNext();) {
            String event = eventIterator.next();
            String domEvent = getDomEvent(event);
            ClientBehaviorContext cbc = ClientBehaviorContext.createClientBehaviorContext(context, (UIComponent) component, event, clientId, params);
            List<ClientBehavior> cbList = behaviorEvents.get(event);
            Boolean writeArray = false;
            JSONBuilder cbJson = JSONBuilder.create();

            if (cbList.size() > 1) {
                writeArray = true;
                cbJson.beginArray();
            }

            for (ClientBehavior cb : cbList) {
                if (cb instanceof javax.faces.component.behavior.AjaxBehavior) continue; // ignore f:ajax

                String script = cb.getScript(cbc);    //could be null if disabled
                if (script != null) {
                    if (writeArray) cbJson.item(script, false); // item: false == no escape
                    else jb.entry(domEvent, script, true); // entry: true == no escape
                }
            }

            if (writeArray) {
                cbJson.endArray();
                jb.entry(domEvent, cbJson.toString(), true);
            }
        }

        jb.endMap();
 // System.out.println(" return back jb object ="+jb.toString());
    }

     private String getDomEvent(String event) {
         String domEvent = event;
         if (event.equalsIgnoreCase("valueChange"))       //editableValueHolder for moharra or myfaces
            domEvent = "change";
         else if(event.equalsIgnoreCase("action"))       //UICommand
            domEvent = "click";
         return domEvent;
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
        String eventBehavior = params.get("javax.faces.behavior.event");
        if(null != eventBehavior) {
          //  logger.info("eventBehavior not null ="+eventBehavior+" for comp="+component.getClientId());
            List<ClientBehavior> eventBehaviorsList = behaviors.get(eventBehavior);
            if (eventBehaviorsList == null || eventBehaviorsList.isEmpty() ){
                return;
            }
            else {
               String source = params.get("javax.faces.source");
               String clientId = component.getClientId();
               if(source != null && source.startsWith(clientId)) {
                   for (ClientBehavior behavior: eventBehaviorsList) {
                       behavior.decode(context, component);
                   }
               }
            }
        } /*else {
            logger.info("eventBehavior NULL for component="+component.getClientId());
        } */
    }
    protected void writeJavascriptFile(FacesContext facesContext,
            UIComponent component, String JS_NAME, String JS_MIN_NAME,
            String JS_LIBRARY, String JS2_NAME, String JS2_MIN_NAME, String JS2_LIB) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = component.getClientId(facesContext);
        writer.startElement(HTML.SPAN_ELEM, component);
        writer.writeAttribute(CLASS_ATTR, "mobi-hidden", null);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_libJS", HTML.ID_ATTR);
        if (!isScriptLoaded(facesContext, JS_NAME)) {
            String jsFname = JS_NAME;
            if (facesContext.isProjectStage(ProjectStage.Production)){
                jsFname = JS_MIN_NAME;
            }
            //set jsFname to min if development stage
            Resource jsFile = facesContext.getApplication().getResourceHandler().createResource(jsFname, JS_LIBRARY);
            String src = jsFile.getRequestPath();
            writer.startElement("script", component);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeAttribute("src", src, null);
            writer.endElement("script");
            setScriptLoaded(facesContext, JS_NAME);
        }
        if (!isScriptLoaded(facesContext, JS2_NAME)) {
            String jsFname = JS2_NAME;
            if (facesContext.isProjectStage(ProjectStage.Production)){
                jsFname = JS2_MIN_NAME;
            }
            //set jsFname to min if development stage
            Resource jsFile = facesContext.getApplication().getResourceHandler().createResource(jsFname, JS2_LIB);
            String src = jsFile.getRequestPath();
            writer.startElement("script", component);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeAttribute("src", src, null);
            writer.endElement("script");
            setScriptLoaded(facesContext, JS2_NAME);
        }
        writer.endElement(HTML.SPAN_ELEM);
    }

    protected void writeJavascriptFile(FacesContext facesContext, 
            UIComponent component, String JS_NAME, String JS_MIN_NAME, 
            String JS_LIBRARY) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = component.getClientId(facesContext);
        writer.startElement(HTML.SPAN_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_libJS", HTML.ID_ATTR);
        writer.writeAttribute(CLASS_ATTR, "mobi-hidden", null);
        if (!isScriptLoaded(facesContext, JS_NAME)) {
            String jsFname = JS_NAME;
            if (facesContext.isProjectStage(ProjectStage.Production)){
                jsFname = JS_MIN_NAME;
            }
            //set jsFname to min if development stage
            Resource jsFile = facesContext.getApplication().getResourceHandler().createResource(jsFname, JS_LIBRARY);
            String src = jsFile.getRequestPath();
            writer.startElement("script", component);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeAttribute("src", src, null);
            writer.endElement("script");
            setScriptLoaded(facesContext, JS_NAME);
        } 
        writer.endElement(HTML.SPAN_ELEM);
    }

    protected void setScriptLoaded(FacesContext facesContext, 
            String JS_NAME) {
        InlineScriptEventListener.setScriptLoaded(facesContext, JS_NAME);
    }

    protected boolean isScriptLoaded(FacesContext facesContext, String JS_NAME) {
        return InlineScriptEventListener.isScriptLoaded(facesContext, JS_NAME);
    }
}
