package org.icefaces.component.checkbox;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIOutput;

import java.io.IOException;

@ResourceDependencies({
	@ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),	
    @ResourceDependency(name="checkbox.js",library="org.icefaces.component.checkbox")    
})
public class Checkbox extends CheckboxBase {

    public Checkbox() {
        loadDependency(FacesContext.getCurrentInstance());     
    }

	// load required yui libraries
    private void loadDependency(FacesContext context) {
        context.getViewRoot().addComponentResource(context, new UIOutput() {
            public void encodeBegin(FacesContext context) throws IOException {
                ResponseWriter writer = context.getResponseWriter();
				writeCssExternFile(writer, "http://yui.yahooapis.com/2.7.0/build/button/assets/skins/sam/button.css");
                writeJavascriptExternFile(writer, "http://yui.yahooapis.com/2.7.0/build/yahoo-dom-event/yahoo-dom-event.js");
                writeJavascriptExternFile(writer, "http://yui.yahooapis.com/2.7.0/build/element/element-min.js");
                writeJavascriptExternFile(writer, "http://yui.yahooapis.com/2.7.0/build/button/button-min.js");       
            }
        }, "head");        
    }
	
    private void writeJavascriptExternFile(ResponseWriter writer, String url) throws IOException {
        writer.startElement("script", this);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeAttribute("src", url, null);
        writer.endElement("script");
    }
	
    private void writeCssExternFile(ResponseWriter writer, String url) throws IOException {
        writer.startElement("link", this);
        writer.writeAttribute("rel", "stylesheet", null);
        writer.writeAttribute("type", "text/css", null);        
        writer.writeAttribute("href", url, null);
        writer.endElement("link");
    }  
	
	// return false if current value is null?
    public Object getValue() {
	
		Object value = super.getValue();
		if (value == null) return Boolean.valueOf(false);
		
		return value;
	}
}
