package org.icefaces.component.sliderentry;

import org.icefaces.component.utils.Utils;
import org.icefaces.impl.util.Util;

import java.io.IOException;

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

//Does JSF respect order?
//TODO make it so resources can be coalesce and served as whole 

public class SliderEntry extends SliderEntryBase{
    
    public void encodeBegin(FacesContext context) throws IOException {
        super.encodeBegin(context);
    }
    
    public void broadcast(FacesEvent event)
    throws AbortProcessingException {
        super.broadcast(event);

        //event was fired by me
        if (event != null) {
            
            //To keep it simple slider uses the broadcast to update value, so it doesn't
            //have to keep submitted value
            
            //1. update the value
            ValueExpression ve = getValueExpression("value");
            if (ve != null) {
                try {
                    setValue((Integer)((ValueChangeEvent)event).getNewValue());
                } catch (ELException ee) {
                    ee.printStackTrace();
                }
            } else {
                setValue((Integer)((ValueChangeEvent)event).getNewValue());
            }
            //invoke a valuechange listener if any
            MethodExpression method = getValueChangeListener();
            if (method != null) {
                method.invoke(getFacesContext().getELContext(), new Object[]{event});
            }
        }
    }
    
    public void queueEvent(FacesEvent event) {
        if (isImmediate()) {
            event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        }
        else {
            event.setPhaseId(PhaseId.INVOKE_APPLICATION);
        }
        super.queueEvent(event);
    } 

    
    public String getThumbUrl() {
    	String thumbUrl = super.getThumbUrl();
    	if (null == thumbUrl) {
    		if ("x".equals(getAxis())) {
    			thumbUrl =  "javax.faces.resource/assets/skins/sam/thumb-x.png.jsf?ln=yui/3_2_0";
    		} else {
    			thumbUrl = "javax.faces.resource/assets/skins/sam/thumb-y.png.jsf?ln=yui/3_2_0";
    		}
    	}
    	return thumbUrl;
    }
    
    public boolean isSingleSubmit() {
        return Utils.superValueIfSet(this, getStateHelper(), PropertyKeys.singleSubmit.name(), super.isSingleSubmit(), Util.withinSingleSubmit(this));
    }
}
