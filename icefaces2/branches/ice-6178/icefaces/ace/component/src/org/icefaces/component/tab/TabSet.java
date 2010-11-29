package org.icefaces.component.tab;

import java.io.IOException;

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

public class TabSet extends TabSetBase {
    
    public TabSet() {
        //loadDependency(FacesContext.getCurrentInstance());        
    }

    public void broadcast(FacesEvent event)
    throws AbortProcessingException {
        super.broadcast(event);
        if (event != null) {
            ValueExpression ve = getValueExpression("selectedIndex");
            if(isCancelOnInvalid()) {
                getFacesContext().renderResponse();
            }

            if (ve != null) {
                try {
                    ve.setValue(getFacesContext().getELContext(), ((ValueChangeEvent)event).getNewValue());
                } catch (ELException ee) {
                    ee.printStackTrace();
                }
            } else {
                setSelectedIndex((Integer)((ValueChangeEvent)event).getNewValue());
            }
            ValueChangeEvent e = (ValueChangeEvent)event;
            MethodExpression method = getTabChangeListener();
            if (method != null) {
                method.invoke(getFacesContext().getELContext(), new Object[]{event});
            }
        }
    }
    
    public void queueEvent(FacesEvent event) {
        if (event.getComponent() instanceof TabSet) {
            if (isImmediate() || !isCancelOnInvalid()) {
                event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            }
            else {
                event.setPhaseId(PhaseId.INVOKE_APPLICATION);
            }
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
}
