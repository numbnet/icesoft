package org.icefaces.component.slider;

import java.io.IOException;

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;

@ResourceDependencies({
    @ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="yui3.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="slider.js",library="org.icefaces.component.slider")    
})
public class Slider extends SliderBase{
    
    public Slider() {
        loadDependency(FacesContext.getCurrentInstance());     
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
        super.encodeBegin(context);
    }

    private void loadDependency(FacesContext context) {
        context.getViewRoot().addComponentResource(context, new UIOutput() {
            public void encodeBegin(FacesContext context) throws IOException {
                ResponseWriter writer = context.getResponseWriter();
                writer.startElement("script", this);
                writer.writeAttribute("type", "text/javascript", null);
                writer.writeAttribute("src", "http://yui.yahooapis.com/3.0.0/build/yui/yui-min.js", null);
                writer.endElement("script");              
            }
        }, "head");        
    }
    
    
    public void broadcast(FacesEvent event)
    throws AbortProcessingException {
        super.broadcast(event);
        if (event != null) {
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

            MethodExpression method = getValueChangeListener();
            if (method != null) {
                method.invoke(getFacesContext().getELContext(), new Object[]{event});
            }
        }
    }
    
}
