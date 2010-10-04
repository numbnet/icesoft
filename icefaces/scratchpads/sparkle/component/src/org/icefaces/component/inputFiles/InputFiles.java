/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package org.icefaces.component.inputFiles;

import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.AbortProcessingException;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.MethodExpression;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class InputFiles extends InputFilesBase {
    private static final String INFO_KEY = "org.icefaces.component.inputFiles.infos";
    private static final String EVENT_KEY = "org.icefaces.component.inputFiles.events";
    
    public InputFiles() {
        super();
    }
    
    public void setInfo(InputFilesInfo info) {
        try {
            super.setInfo(info);
        }
        catch(RuntimeException e) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            if (facesContext.isProjectStage(ProjectStage.Development) ||
                facesContext.isProjectStage(ProjectStage.UnitTest)) {
                System.out.println("Problem setting info property on " +
                    "InputFiles component: " + e);
                e.printStackTrace();
            }
            throw e;
        }
    }
    
    public InputFilesInfo getInfo() {
        InputFilesInfo info = super.getInfo();
        if (info != null) {
            info = (InputFilesInfo) info.clone();
        }
        return info;
    }
    
    public void reset() {
        setInfo(null);
    }

    /**
     * Apparently, the view state id changes, if you try to get it early in 
     * the lifecycle. So, you can only call this method later on, while 
     * rendering. As such, it's only really applicable from storeConfig(-)
     * 
     * By definition, this method must encode both the clientId and the
     * view state id, so that both parts can be extracted out by the 
     * InputFilesPhaseListener. It's used as a key into the session map, to 
     * store the InputFilesConfig. As well, it has to be a valid HTML form 
     * field id and name. From the HTML 4 and XHTML 1 specs: ID and NAME 
     * tokens must begin with a letter ([A-Za-z]) and may be followed by any 
     * number of letters, digits ([0-9]), hyphens ("-"), underscores ("_"), 
     * colons (":"), and periods (".").
     * 
     * TODO
     * An alternative implementation, that would not rely on the view session 
     * id, would involve using a sequence number in the session, so that new
     * inputFiles components would take a sequence number from the session, 
     * and then hold onto that, using state saving. There might even be a 
     * way to use view or page scope, to hold the identifier, using the 
     * clientId as a key, so that if the inputFiles component is removed and 
     * re-added to the view, it would retain the original identifier.
     * 
     * TODO
     * One way to remove some of the necessity for always consistent 
     * identifiers is for a phase listener to remove the config at the 
     * beginning of the lifecycle, so that if it's stored under a new 
     * identifier at the end of the lifecycle, then the old one is not leaked. 
     */
    private String getIdentifier(FacesContext facesContext, String clientId) {
        String viewState = facesContext.getApplication().getStateManager().
            getViewState(facesContext);
        // I couldn't find a character that's valid in an HTML id/name, but 
        // not in a clientId, do just going with a double colon delimiter.
        String id = clientId + "::" + viewState;
        return id;
    }

    /**
     * Used by InputFilesRenderer to save config for InputFilesPhaseListener
     * to use on the next postback
     */
    InputFilesConfig storeConfigForNextLifecycle(FacesContext facesContext, String clientId) {
        String identifier = getIdentifier(facesContext, clientId);
        InputFilesConfig config = new InputFilesConfig(
            identifier,
            clientId,
            getAbsolutePath(),
            getRelativePath(),
            isUseSessionSubdir(),
            isUseOriginalFilename(),
            null,//TODO getCallback(),
            getMaxTotalSize(),
            getMaxFileSize(),
            getMaxFileCount(),
            isRequired());
        Object sessionObj = facesContext.getExternalContext().getSession(false);
        if (sessionObj != null) {
            synchronized(sessionObj) {
                Map<String,Object> map = facesContext.getExternalContext().getSessionMap();
                map.put(identifier, config);
            }
        }
        return config;
    }
    
    /**
     * Used by InputFilesPhaseListener to retrieve config saved away by 
     * InputFilesRenderer in previous lifecycle
     */
    static InputFilesConfig retrieveConfigFromPreviousLifecycle(FacesContext facesContext, String identifier) {
        InputFilesConfig config = null;
        Object sessionObj = facesContext.getExternalContext().getSession(false);
        if (sessionObj != null) {
            synchronized(sessionObj) {
                Map<String,Object> map = facesContext.getExternalContext().getSessionMap();
                config = (InputFilesConfig) map.get(identifier);
            }
        }
        return config;
    }

    /**
     * Used by InputFilesPhaseListener, before the component treee exists, to 
     * save the outcome of the file uploads, to be retrieved later in the same 
     * lifecycle, once the component tree is in place. 
     */
    static void storeInfosForLaterInLifecycle(
            FacesContext facesContext,
            Map<String,InputFilesInfo> clientId2Info) {
        facesContext.getAttributes().put(INFO_KEY, clientId2Info);
    }

    /**
     * Used by InputFilesRenderer.decode(-) to retrieve each inputFiles
     * component's outcome for file uploads.
     */
    static InputFilesInfo retrieveInfoFromEarlierInLifecycle(
            FacesContext facesContext, String clientId) {
        InputFilesInfo info = null;
        Map<String,InputFilesInfo> clientId2Info = (Map<String,InputFilesInfo>)
            facesContext.getAttributes().get(INFO_KEY);
        if (clientId2Info != null) {
            info = clientId2Info.get(clientId);
        }
        return info;
    }

    /**
     * After the ApplyRequestValues phase, when the inputFiles components 
     * have all retrieved their outcomes for uploaded files, clear the 
     * outcomes away, so we don't leak memory. 
     */
    static void removeInfos(FacesContext facesContext) {
        facesContext.getAttributes().remove(INFO_KEY);
    }
    
    void addMessagesFromInfo(FacesContext facesContext, String clientId, InputFilesInfo info) {
//System.out.println("InputFiles.addMessagesFromInfo  info: " + info);
        if (info != null) {
            ArrayList<InputFilesInfo.FileEntry> files = info.getFiles();
            for (InputFilesInfo.FileEntry fe : files) {
//System.out.println("InputFiles.addMessagesFromInfo    FileEntry: " + fe);
                InputFilesStatus status = fe.getStatus();
                FacesMessage fm = status.getFacesMessage(facesContext, this, fe);
//System.out.println("InputFiles.addMessagesFromInfo    FacesMessage: " + fm);
                facesContext.addMessage(clientId, fm);
            }
        }
    }

    /**
     * @return The label property, if specified, else the clientId
     */
    public String getFacesMessageLabel() {
        String label = getLabel();
        if (label != null && label.length() > 0) {
            return label;
        }
        return getClientId();
    }
    
    @Override
    public void queueEvent(FacesEvent event) {
//System.out.println("InputFiles.queueEvent  clientId: " + getClientId());
        if (isImmediate()) {
            event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
//System.out.println("InputFiles.queueEvent    immediate == true  queuing event: " + event);
            super.queueEvent(event);
        }
        else {
            event.setPhaseId(PhaseId.RENDER_RESPONSE);
//System.out.println("InputFiles.queueEvent    immediate == false  storing event: " + event);
            storeEventForPreRender(event);
        }
    }
    
    @Override
    public void broadcast(FacesEvent event) {
//System.out.println("InputFiles.broadcast  clientId: " + getClientId() + "  event: " + event);
        if (event instanceof InputFilesEvent) {
            MethodExpression inpFilesList = getInputFilesListener();
            if (inpFilesList != null) {
                FacesContext context = FacesContext.getCurrentInstance();
                ELContext elContext = context.getELContext();
                try {
                    inpFilesList.invoke(elContext, new Object[] {event});
                } catch (ELException ee) {
                    throw new AbortProcessingException(ee.getMessage(), ee.getCause());
                }
            }
        }
        else {
            super.broadcast(event);
        }
    }
    
    /**
     * Used by InputFiles.queueEvent(-), to save non-immediate InputFilesEvent
     * objects to be invoked by InputFilesPhaseListener in pre-Render phase. 
     */
    private void storeEventForPreRender(FacesEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String,FacesEvent> clientId2FacesEvent = (Map<String,FacesEvent>)
            facesContext.getAttributes().get(EVENT_KEY);
        if (clientId2FacesEvent == null) {
            clientId2FacesEvent = new HashMap<String,FacesEvent>(6);
            facesContext.getAttributes().put(EVENT_KEY, clientId2FacesEvent);
        }
        clientId2FacesEvent.put(getClientId(facesContext), event);
    }

    /**
     * Used by InputFilesPhaseListener(-) to retrieve each the InputFilesEvent
     * objects, so they can be invoked pre-Render phase.
     */
    static Map<String,FacesEvent> removeEventsForPreRender(
            FacesContext facesContext) {
        Map<String,FacesEvent> clientId2FacesEvent = (Map<String,FacesEvent>)
            facesContext.getAttributes().remove(EVENT_KEY);
        return clientId2FacesEvent;
    }
}
