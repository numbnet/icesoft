package org.icefaces.component.tab;

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
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.component.annotation.Field;

@ResourceDependencies({
    @ResourceDependency(name="yui.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="tabset.js",library="org.icefaces.component.tab"),
    @ResourceDependency(name="tabset.css",library="org.icefaces.component.tab")    
})
public class TabSet extends TabSetBase {
    
    public TabSet() {
        loadDependency(FacesContext.getCurrentInstance());        
    }

    public void encodeBegin(FacesContext context) throws IOException {
        super.encodeBegin(context);
    }
    
    public void encodeEnd(FacesContext context) throws IOException {
        super.encodeEnd(context);

    }
    
    public void processDecodes(FacesContext context) {
        System.out.println("processDecodes " + isValidationFailed());
        if (context == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        super.processDecodes(context);

    }
    
    
    public void processValidators(FacesContext context) {
        System.out.println("processValidators "+ isValidationFailed());  
        if (context == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        super.processValidators(context);
         
    }

    private boolean isValidationFailed() {
       if (getFacesContext().getMessages().hasNext() ||
                getFacesContext().isValidationFailed() 
                )
        return true;
        return false;
    }
    
    public void broadcast(FacesEvent event)
    throws AbortProcessingException {
        System.out.println("1. >>>>>>>>>>>>>> Broadcast" + event.getPhaseId());
        super.broadcast(event);
        if (event != null) {
            System.out.println("2. >>>>>>>>>>>>>> event found");
            ValueExpression ve = getValueExpression("tabIndex");
            if(isCancelOnInvalid()) {
                getFacesContext().renderResponse();
                System.out.println("3. >>>>>>>>>>>>>> render response");
            }

            if (ve != null) {
                Throwable caught = null;
                FacesMessage message = null;
                try {
                    ve.setValue(getFacesContext().getELContext(), ((ValueChangeEvent)event).getNewValue());
                  System.out.println("Setting to MINVALUE "+ ((ValueChangeEvent)event).getNewValue());
                } catch (ELException ee) {
                    System.out.println(ee);
                }
            } else {
                setTabIndex((Integer)((ValueChangeEvent)event).getNewValue());
            }
            ValueChangeEvent e = (ValueChangeEvent)event;
            MethodExpression method = getTabChangeListener();
            if (method != null) {
                method.invoke(getFacesContext().getELContext(), new Object[]{event});
            }
        }
    }
    
    public void setTabIndex(int tabindex) {
        System.out.println("Setting tab Index  = "+ tabindex);
        super.setTabIndex(tabindex);
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