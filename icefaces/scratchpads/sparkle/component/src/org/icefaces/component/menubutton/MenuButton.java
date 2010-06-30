package org.icefaces.component.menubutton;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIOutput;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import java.io.IOException;

@ResourceDependencies({
	@ResourceDependency(name = "sam/menu/fonts-min.css", library = "org.icefaces.component.sprites"),
    @ResourceDependency(name = "sam/menu/menu.css", library = "org.icefaces.component.sprites"),
    @ResourceDependency(name = "sam/button/button.css", library = "org.icefaces.component.sprites"),
    @ResourceDependency(library = "yui/2_8_1", name="yuiloader/yuiloader-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name="yuiloader/dom-min.js"),   
    @ResourceDependency(library = "yui/2_8_1", name = "event/event-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "container/container_core-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "menu/menu.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "element/element-min.js"),   
    @ResourceDependency(library = "yui/2_8_1", name = "button/button-min.js"),
	@ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),	
    @ResourceDependency(name="menubutton.js",library="org.icefaces.component.menubutton")    
})
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
