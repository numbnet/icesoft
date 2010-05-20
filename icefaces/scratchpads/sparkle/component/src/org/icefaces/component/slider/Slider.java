package org.icefaces.component.slider;

import java.io.IOException;

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.component.tab.TabSet;

//Does JSF respect order?
//TODO make it so resources can be coalesce and served as whole 
@ResourceDependencies({
    @ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="yui3.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="slider.js",library="org.icefaces.component.slider"),
	@ResourceDependency(name="yui/yui-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="loader/loader-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="oop/oop-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="yui/yui-later-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="event-custom/event-custom-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="attribute/attribute-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="event/event-base-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="pluginhost/pluginhost-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="dom/dom-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="node/node-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="event/event-delegate-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="event/event-focus-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="base/base-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="classnamemanager/classnamemanager-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="widget/widget-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="yui/yui-throttle-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="dd/dd-ddm-base-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="dd/dd-drag-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="dd/dd-constrain-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="dump/dump-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="substitute/substitute-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="slider/slider-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="intl/intl-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="slider/assets/skins/sam/slider.css",library="yui/3_1_1"),
	@ResourceDependency(name="widget/assets/skins/sam/widget.css",library="yui/3_1_1")
})
public class Slider extends SliderBase{
    
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
                    ve.setValue(getFacesContext().getELContext(), ((ValueChangeEvent)event).getNewValue());
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
}
