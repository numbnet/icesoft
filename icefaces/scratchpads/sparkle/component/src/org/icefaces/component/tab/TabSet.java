package org.icefaces.component.tab;

import java.io.IOException;
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
    
    MethodBinding tabChangeListener;
    
    public UIComponent getHeadFacet() {
        return (UIComponent) getFacet("header");
    }

    public UIComponent getBodyFacet() {
        return (UIComponent) getFacet("body");
    }
    
    public UIComponent getFooterFacet() {
        return (UIComponent) getFacet("footer");
    }

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
    
    public void setClientSide(boolean clientSide) {
        this.clientSide = new Boolean(clientSide);
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
            MethodBinding method = getTabChangeListener();
            if (method != null) {
                method.invoke(getFacesContext(), new Object[]{event});
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
    
    public MethodBinding getTabChangeListener() {
        return tabChangeListener;
    }

    public void setTabChangeListener(MethodBinding tabChangeListener) {
        this.tabChangeListener = tabChangeListener;
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
