package org.icefaces.component.tab;

import java.io.IOException;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

@ResourceDependencies({
    @ResourceDependency(name="yui.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="tabset.js",library="org.icefaces.component.tab"),
    @ResourceDependency(name="tabset.css",library="org.icefaces.component.tab")    
})
public class TabSet extends TabSetBase {
    private String orientation;
 
    public TabSet() {
        loadDependency(FacesContext.getCurrentInstance());        
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
        //TODO need to be changed in the processUpdate. However this property is not 
        //involved in any validator or convertor so it will not harm 
        ValueBinding vb = getValueBinding("orientation");
        if (vb != null) {
            vb.setValue(getFacesContext(), this.orientation);
            this.orientation = null;
        }        
    }

    public String getOrientation() {
        if (orientation != null) {
            return orientation;
        }
        ValueBinding vb = getValueBinding("orientation");
        return vb != null ? ((String) vb.getValue(getFacesContext())) : "top";
    }    

    public void encodeBegin(FacesContext context) throws IOException {
        super.encodeBegin(context);
    }
    
    public void encodeEnd(FacesContext context) throws IOException {
        super.encodeEnd(context);

    }

    public void broadcast(FacesEvent event)
    throws AbortProcessingException {
        super.broadcast(event);
        if (event != null) {
            ValueChangeEvent e = (ValueChangeEvent)event;
            System.out.println("Broad Cast changing tabindex .......");
            //setTabIndex(((Integer)e.getNewValue()).intValue());
            MethodExpression method = getTabChangeListener();
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
    
    private void loadDependency(FacesContext context) {
        context.getViewRoot().addComponentResource(context, new UIOutput() {
            public void encodeBegin(FacesContext context) throws IOException {
                ResponseWriter writer = context.getResponseWriter();
                writeCssExternFile(writer, "http://yui.yahooapis.com/2.7.0/build/fonts/fonts-min.css");
                writeCssExternFile(writer, "http://yui.yahooapis.com/2.7.0/build/tabview/assets/skins/sam/tabview.css");                
                writeJavascriptExternFile(writer, "http://yui.yahooapis.com/2.7.0/build/yahoo-dom-event/yahoo-dom-event.js");
                writeJavascriptExternFile(writer, "http://yui.yahooapis.com/2.7.0/build/connection/connection-min.js");
                writeJavascriptExternFile(writer, "http://yui.yahooapis.com/2.7.0/build/element/element-min.js");
                writeJavascriptExternFile(writer, "http://yui.yahooapis.com/2.7.0/build/tabview/tabview-min.js");
                writeInlineStyle(writer);                
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
    
    private void writeInlineStyle(ResponseWriter writer)  throws IOException {
        writer.startElement("style", this);
        writer.writeAttribute("type", "text/css", null);        
        writer.write(".iceOutConStatActv {background-color: transparent;"+
                "background-image: url( \"images/connect_active.gif\" );"+
                "background-repeat: no-repeat;"+
                "}");
        writer.endElement("style");        
    }
    
    private Object[] values;

    public Object saveState(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (values == null) {
            values = new Object[2];
        }

        values[0] = super.saveState(context);
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (state == null) {
            return;
        }
        values = (Object[]) state;
        super.restoreState(context, values[0]);
    }
}