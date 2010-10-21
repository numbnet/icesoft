package org.icefaces.component.menubutton;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIOutput;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import java.io.IOException;

public class MenuButton extends MenuButtonBase {
	
//	   public void broadcast(FacesEvent event)
//	    throws AbortProcessingException {
//	        super.broadcast(event);
//	        if (event != null) {
//	            ValueExpression ve = getValueExpression("selectedMenuItem");
//	            if(isCancelOnInvalid()) {
//	                getFacesContext().renderResponse();
//	            }
//
//	            if (ve != null) {
//	                try {
//	                    ve.setValue(getFacesContext().getELContext(), ((ValueChangeEvent)event).getNewValue());
//	                } catch (ELException ee) {
//	                    ee.printStackTrace();
//	                }
//	            } else {
//	                setSelectedMenuItem((String)((ValueChangeEvent)event).getNewValue());
//	            }
//	            ValueChangeEvent e = (ValueChangeEvent)event;
//	            MethodExpression method = getMenuSelectionListener();
//	            if (method != null) {
//	                method.invoke(getFacesContext().getELContext(), new Object[]{event});
//	            }
//	        }
//	    }
//	    
//	    public void queueEvent(FacesEvent event) {
//	        if (event.getComponent() instanceof MenuButton) {
//	            if (isImmediate() || !isCancelOnInvalid()) {
//	                event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
//	            }
//	            else {
//	                event.setPhaseId(PhaseId.INVOKE_APPLICATION);
//	            }
//	        }
//	        super.queueEvent(event);
//	    }  


}
