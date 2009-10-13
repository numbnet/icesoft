package org.icefaces.component.tab;

import java.io.IOException;

import javax.el.MethodExpression;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

public class TabSet extends TabSetBase {
 //   private static Resource JS = new TabSetJarResource("com/icesoft/faces/component/yui/tab/tabset.js");
    private Integer tabIndex;
    int oldTabIndex = Integer.MIN_VALUE;    
    private String orientation;
    String oldOrientation;   


//    private void loadJs() {
//        //register the ICEfaces provided JS to be loaded
//        ResourceRegistry registry =
//            (ResourceRegistry) FacesContext.getCurrentInstance();
//            registry.loadJavascriptCode(JS);
//    }
    
    public void setTabIndex(int tabindex) {
        this.tabIndex = new Integer(tabindex);
        //TODO need to be changed in the processUpdate. However this property is not 
        //involved in any validator or convertor so it will not harm 
        ValueBinding vb = getValueBinding("tabIndex");
        if (vb != null) {
            vb.setValue(getFacesContext(), this.tabIndex);
            this.tabIndex = null;
        }        
    }

    public int getTabIndex() {
        if (tabIndex != null) {
            return tabIndex.intValue();
        }
        ValueBinding vb = getValueBinding("tabIndex");
        return vb != null ? ((Integer) vb.getValue(getFacesContext())).intValue() : 0;
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
        //load js.js, will be loaded once per view. It handled by the ICEFaces framework.
       // loadJs();
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
            setTabIndex(((Integer)e.getNewValue()).intValue());
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
    
    
    private Object[] values;

    public Object saveState(FacesContext facesContext) {

        if (facesContext == null) {
            throw new NullPointerException();
        }
        if (values == null) {
            values = new Object[3];
        }
        
        values[0] = super.saveState(facesContext);
        values[1] = oldOrientation; 
        values[2] = Integer.valueOf(oldTabIndex); 
        return (values);        
    }
    
    public void restoreState(FacesContext facesContext, Object state) {

        if (facesContext == null) {
            throw new NullPointerException();
        }

        if (state == null) {
            return;
        }
        values = (Object[]) state;
        super.restoreState(facesContext, values[0]);
        oldOrientation = (String) values[1];
        oldTabIndex = ((Integer) values[2]).intValue();        
    }
}

//class TabSetJarResource extends JarResource {
//
//    public TabSetJarResource(String path) {
//        super(path);
//    }
//    
//    public void withOptions(Options options) throws IOException {
//    
//    }
//}
