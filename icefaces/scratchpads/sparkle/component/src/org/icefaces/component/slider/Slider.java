package org.icefaces.component.slider;

import java.io.IOException;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

@ResourceDependencies({
    @ResourceDependency(name="yui3.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="util.js",library="org.icefaces.component.util"),
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
}
