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

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * The purpose of this class is to forward propogate FacesMessage objects
 * across partialSubmit, singleSubmit, server push lifecycles. The issue being 
 * that, in a previous lifecycle, components may have added FacesMessages to 
 * the FacesContext due to failed validation, or the application may have 
 * programmatically added FacesMessages to the FacesContext, and then these 
 * inadvertent lifecycles occur, which not only remove all previous 
 * FacesMessage objects, but which also only execute subsets of the component 
 * tree, not giving the unexecuted components, nor the already invoked 
 * application code, a chance to re-add their FacesMessage objects to the 
 * FacesContext. This class saves away all FacesMessage objects, at the end 
 * of a lifecycle, to re-add these specific ones, before rendering, of the 
 * next lifecycle.
 */
public class FacesMessagesPhaseListener implements PhaseListener {
    private static final Logger LOGGER = Logger.getLogger(FacesMessagesPhaseListener.class.getName());

    private static final String SAVED_GLOBAL_FACES_MESSAGES_KEY =
        "org.icefaces.event.saved_global_faces_messages";
    private static final String SAVED_COMPONENT_FACES_MESSAGES_KEY =
        "org.icefaces.event.saved_component_faces_messages";
    
    public void afterPhase(PhaseEvent phaseEvent) {
//System.out.println("-------------------------------------");
//System.out.println("FacesMessagesPhaseListener.afterPhase: " + phaseEvent.getPhaseId());
        // Save away all of the FacesMessage objects, so that next lifecycle,
        // the appropriate ones may be put back into the FacesContext.
        FacesContext facesContext = phaseEvent.getFacesContext();
//dumpMaps(facesContext);
    }
    
    protected void saveFacesMessages(FacesContext facesContext) {
//System.out.println("saveFacesMessages()");
        List<FacesMessage> globals = facesContext.getMessageList(null);
//System.out.println("  global FacesMessage(s): " + toStringListOfFacesMessages(globals));
        
        Map<String, List<FacesMessage>> components = new LinkedHashMap<String, List<FacesMessage>>(6);
        Iterator<String> cids = facesContext.getClientIdsWithMessages();
        while (cids.hasNext()) {
            String clientId = cids.next();
            if (clientId == null) {
                continue;
            }
//System.out.println("  clientId of FacesMessage(s): '" + clientId + "'");
            List<FacesMessage> msgs = facesContext.getMessageList(clientId);
            components.put(clientId, msgs);
//System.out.println("      list of FacesMessage(s): " + toStringListOfFacesMessages(msgs));
        }

        if (!globals.isEmpty()) {
            facesContext.getViewRoot().getAttributes().put(
                SAVED_GLOBAL_FACES_MESSAGES_KEY, globals);
        }
        if (!components.isEmpty()) {
            facesContext.getViewRoot().getAttributes().put(
                SAVED_COMPONENT_FACES_MESSAGES_KEY, components);
        }
//System.out.println("  After saving away the faces messages");
//dumpMaps(facesContext);
    }

    public void beforePhase(PhaseEvent phaseEvent) {
//System.out.println("======================================");
//System.out.println("======================================");
//System.out.println("FacesMessagesPhaseListener.beforePhase: " + phaseEvent.getPhaseId());
        // Restore any previously saved FacesMessage objects, back into the
        // FacesContext, if they meet the following criteria:
        // A. Have a clientId, and so are associated to a component
        //    Components NOT executed (not full execute, and not in partial execute subtree)
        //    (Possibly) Components still invalid
        // B. Global messages, not associated to a component 
        //    A partial execute, not a full execute
        FacesContext facesContext = phaseEvent.getFacesContext();
        Map viewMap = facesContext.getViewRoot().getViewMap();
        Map initParameterMap = facesContext.getExternalContext().getInitParameterMap();
        if (!EnvUtils.isMessagePersistence(facesContext) ||
            (viewMap.containsKey(EnvUtils.MESSAGE_PERSISTENCE) && !((Boolean)viewMap.get(EnvUtils.MESSAGE_PERSISTENCE)).booleanValue())) {

            return;
        }
//dumpMaps(facesContext);
////if(!phaseEvent.getPhaseId().equals(PhaseId.RENDER_RESPONSE))return;
        
        restoreFacesMessages(facesContext);
        saveFacesMessages(facesContext);
    }
    
    protected void restoreFacesMessages(FacesContext facesContext) {
//System.out.println("restoreFacesMessages()");
//System.out.println("    isAjaxRequest   : " + facesContext.getPartialViewContext().isAjaxRequest());
//System.out.println("    isPartialRequest: " + facesContext.getPartialViewContext().isPartialRequest());
//System.out.println("    isExecuteAll    : " + facesContext.getPartialViewContext().isExecuteAll());
        List<FacesMessage> globals = (List<FacesMessage>)
            facesContext.getViewRoot().getAttributes().remove(
                SAVED_GLOBAL_FACES_MESSAGES_KEY);
        Map<String, List<FacesMessage>> components = 
            (Map<String, List<FacesMessage>>)
                facesContext.getViewRoot().getAttributes().remove(
                    SAVED_COMPONENT_FACES_MESSAGES_KEY);
        
        if (globals != null && globals.size() > 0) {
//System.out.println("  globals.size: " + globals.size());
            if (!facesContext.getPartialViewContext().isExecuteAll()) {
//System.out.println("    execute all: " + facesContext.getPartialViewContext().isExecuteAll());
                List<FacesMessage> newGlobals = facesContext.getMessageList(null);
//System.out.println("    new globals: " + toStringListOfFacesMessages(newGlobals));
                for (FacesMessage fm : globals) {
//System.out.println("    old global fm: " + toStringFacesMessage(fm));
                    //TODO Check that this actually eliminated redundant additions
                    if (newGlobals.contains(fm)) {
                        continue;
                    }
                    boolean matchedSummaryAndDetail = false;
                    for (FacesMessage newGlobal : newGlobals) {
                        if (stringEquals(newGlobal.getSummary(), fm.getSummary()) &&
                            stringEquals(newGlobal.getDetail(), fm.getDetail())) {
                            matchedSummaryAndDetail = true;
                            break;
                        }
                    }
                    if (!matchedSummaryAndDetail) {
//System.out.println("      old global carried forward");
                        facesContext.addMessage(null, fm);
                    }
                }
            }
        }
        
        if (components != null && components.size() > 0) {
//System.out.println("  components.size: " + components.size());
            //TODO Handle the case where it is a full execute, but another form was submitted
            if (facesContext.getPartialViewContext().isExecuteAll()) {
//System.out.println("    execute all: " + facesContext.getPartialViewContext().isExecuteAll());
            }
            else {
                /*
                char sep = UINamingContainer.getSeparatorChar(facesContext);
                */
                Collection<String> executeIds =
                    facesContext.getPartialViewContext().getExecuteIds();
//System.out.println("    executeIds: " + executeIds);
                
                // Determine which components were executed
                //TODO Possibly check if component still invalid. Hopefully don't have to check this
                Map<String, Boolean> clientId2Executed =
                    new HashMap<String, Boolean>(components.size());
                Set<String> clientIds = components.keySet();
                EnumSet<VisitHint> hints = EnumSet.of(
                    VisitHint.SKIP_UNRENDERED);
                VisitContext visitContext = VisitContext.createVisitContext(
                    facesContext, clientIds, hints);
                VisitCallback vcall = new ComponentsExecutedByExecuteId(
                    executeIds, clientId2Executed);
                facesContext.getViewRoot().visitTree(
                    visitContext, vcall);
                
                // Re-add the FacesMessage(s) for the unexecuted components
                for (String clientId : clientIds) {
//System.out.println("      clientId: " + clientId);
                    Boolean executedResult = clientId2Executed.get(clientId);
                    boolean executed = (executedResult != null &&
                                        executedResult.booleanValue());
                    /*
                    for (String execId : executeIds) {
                        if (execId.length() > 0 &&
                            (clientId.equals(execId) || clientId.startsWith(execId + sep))) {
System.out.println("        clientId startsWith execId: " + execId);
                            executed = true;
                            break;
                        }
                    }
                    */
                    if (!executed) {
                        List<FacesMessage> msgs = components.get(clientId);
                        List<FacesMessage> existingMsgs =
                            facesContext.getMessageList(clientId);
                        for (FacesMessage fm : msgs) {
//System.out.println("        considering adding fm: " + toStringFacesMessage(fm));
                            
                            boolean matchedSummaryAndDetail = false;
                            if (existingMsgs != null) {
                                for (FacesMessage existing : existingMsgs) {
                                    if (stringEquals(existing.getSummary(),
                                            fm.getSummary()) &&
                                        stringEquals(existing.getDetail(),
                                            fm.getDetail()))
                                    {
                                        matchedSummaryAndDetail = true;
                                        break;
                                    }
                                }
                            }
//System.out.println("          matchedSummaryAndDetail: " + matchedSummaryAndDetail);
                            if (!matchedSummaryAndDetail) {
//System.out.println("          adding");
                                facesContext.addMessage(clientId, fm);
                            }
                        }
                    }
                }
            }
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
    
    private static boolean isServerPush(FacesContext facesContext) {
        Map<String, String> rpm = facesContext.getExternalContext().getRequestParameterMap();
        if (rpm.containsKey("ice.submit.type")) {
            String type = rpm.get("ice.submit.type");
            if (type.equals("ice.push")) {
                return true;
            }
        }
        return false;
    }
    
    private static String toStringListOfFacesMessages(List<FacesMessage> msgs) {
        StringBuilder sb = new StringBuilder();
        if (msgs == null) {
            sb.append("null");
        }
        else {
            sb.append('[');
            for (int i = 0; i < msgs.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(toStringFacesMessage(msgs.get(i)));
            }
            sb.append(']');
        }
        return sb.toString();
    }
    
    private static String toStringFacesMessage(FacesMessage fm) {
        StringBuilder sb = new StringBuilder();
        if (fm == null) {
            sb.append("null");
        }
        else {
            sb.append(fm.getClass().getName());
            sb.append('(');
            sb.append("sev='");
            sb.append(fm.getSeverity().toString());
            sb.append("', summary='");
            sb.append(fm.getSummary());
            sb.append("', detail='");
            sb.append(fm.getDetail());
            sb.append("')");
        }
        return sb.toString();
    }
    
    private static boolean stringEquals(String s1, String s2) {
        if (s1 == s2) {
            return true;
        }
        else if (s1 == null && s2 != null) {
            return false;
        }
        else if (s1 != null && s2 == null) {
            return false;
        }
        else {
            return s1.equals(s2);
        }
    }
    
    private static void dumpMaps(FacesContext facesContext) {
        UIViewRoot viewRoot = facesContext.getViewRoot();
        if (viewRoot != null) {
            List<FacesMessage> globals = (List<FacesMessage>)
                viewRoot.getAttributes().get(SAVED_GLOBAL_FACES_MESSAGES_KEY);
            Map<String, List<FacesMessage>> components = (Map<String, List<FacesMessage>>)
                viewRoot.getAttributes().get(SAVED_COMPONENT_FACES_MESSAGES_KEY);
//System.out.println("globals: " + globals);
//System.out.println("components: " + components);
        }
    }
    
    
    private static class ComponentsExecutedByExecuteId
            implements VisitCallback {
        private Collection<String> executeIds;
        private Map<String, Boolean> clientId2Executed;
        
        ComponentsExecutedByExecuteId(
            Collection<String> executeIds,
            Map<String, Boolean> clientId2Executed) {
            this.executeIds = executeIds;
            this.clientId2Executed = clientId2Executed;
        }
        
        public VisitResult visit(
            VisitContext visitContext, UIComponent uiComponent) {
            FacesContext facesContext = visitContext.getFacesContext();
            String clientId = uiComponent.getClientId(facesContext);
            boolean executed = false;
            
            UIComponent currComp = uiComponent;
            String currClientId = clientId;
            while (true) {
                if (executeIds.contains(currClientId)) {
                    executed = true;
                    break;
                }
                currComp = currComp.getParent();
                if (currComp == null || currComp instanceof UIViewRoot) {
                    break;
                }
                currClientId = currComp.getClientId(facesContext);
            }
            
            clientId2Executed.put(clientId, executed);
            return VisitResult.ACCEPT;
        }
    }
}
