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

package org.icefaces.mobi.component.accordion;


import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.MethodExpression;
import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.mobi.api.ContentPaneController;
import org.icefaces.mobi.renderkit.InlineScriptEventListener;
import org.icefaces.mobi.utils.JSFUtils;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.component.IAccordion;
import org.icemobile.util.ClientDescriptor;

public class Accordion extends AccordionBase implements ContentPaneController, IAccordion {
    
    public static final String JS_LIBRARY = "org.icefaces.component.accordion";
    
    private static Logger logger = Logger.getLogger(Accordion.class.getName());
    private String hashVal;
    /**
     * method is required by ContentPaneController interface
     * returns null if their are no children of type contentPane or no children at all.
     * If activeIndex is outside of the range of 0 -> number of children -1, then the default
     * valid value is the first child.
     */
    public Accordion(){
        super();
    }

    public void setHashVal(String hashVal){
        this.hashVal = hashVal;
    }

    public String getHashVal(){
        return this.hashVal;
    }


     public void queueEvent(FacesEvent event) {
       if (event.getComponent() == this) {
           if (logger.isLoggable(Level.FINEST)){
               logger.finest("invoked event for Accordion with selectedId= " + this.getSelectedId());
           }   //no immediate for this component
           event.setPhaseId(PhaseId.INVOKE_APPLICATION);
       }
       super.queueEvent(event);
     }

     public void broadcast(javax.faces.event.FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        MethodExpression me = getPaneChangeListener();
        if(me != null && event instanceof ValueChangeEvent) {
            me.invoke(facesContext.getELContext(), new Object[] {event});
        }
     }

    public ClientDescriptor getClient() {
        return MobiJSFUtils.getClientDescriptor();
    }

    public boolean isProductionProjectStage() {
        return FacesContext.getCurrentInstance().isProjectStage(ProjectStage.Production);
    }

    public String getJavascriptFileRequestPath() {
        String jsFname = JS_NAME;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (FacesContext.getCurrentInstance().isProjectStage(ProjectStage.Production)){
            jsFname = JS_MIN_NAME;
        }
        //set jsFname to min if development stage
        Resource jsFile = facesContext.getApplication().getResourceHandler()
                .createResource(jsFname, JS_LIBRARY);
        return jsFile.getRequestPath();
    }


    public String getOpenedPaneClientId() {
        UIComponent openPane = null;  //all children must be panels
        String currentId = getSelectedId();

        if (getChildCount() <= 0){
            logger.finer("this component must have panels defined as children. Please read DOCS.");
                return null;
        } 
        //check whether we have exceeded maximum number of children for accordion???
        openPane = JSFUtils.getChildById(this, currentId);
        String clId = null;
        if (openPane !=null){
            clId = openPane.getClientId(FacesContext.getCurrentInstance());
        }
        return clId;
    }

}
