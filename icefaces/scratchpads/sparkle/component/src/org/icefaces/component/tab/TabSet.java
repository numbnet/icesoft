package org.icefaces.component.tab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

import com.icesoft.faces.component.event.PropertyChangeEvent;
import com.icesoft.faces.context.JarResource;
import com.icesoft.faces.context.Resource;
import com.icesoft.faces.context.ResourceRegistry;
import com.icesoft.faces.context.Resource.Options;

public class TabSet extends UICommand{
    public static final String COMPONENT_TYPE = "com.icesoft.faces.TabSet";
    public static final String RENDERER_TYPE = "com.icesoft.faces.TabSetRenderer";
    private static Resource JS = new TabSetJarResource("com/icesoft/faces/component/yui/tab/tabset.js");
    private Integer tabIndex;
    int oldTabIndex = Integer.MIN_VALUE;    
    private String orientation;
    String oldOrientation;    
    public Boolean clientSide; 
    public Boolean partialSubmit;
    private String onupdate;
    private String styleClass;
    private String style;
    MethodBinding tabChangeListener;
    
    
    public TabSet() {
        super();
        setRendererType(RENDERER_TYPE);
    }
    
    public UIComponent getHeadFacet() {
        return (UIComponent) getFacet("header");
    }

    public UIComponent getBodyFacet() {
        return (UIComponent) getFacet("body");
    }
    
    public UIComponent getFooterFacet() {
        return (UIComponent) getFacet("footer");
    }

    private void loadJs() {
        //register the ICEfaces provided JS to be loaded
        ResourceRegistry registry =
            (ResourceRegistry) FacesContext.getCurrentInstance();
            registry.loadJavascriptCode(JS);
    }
    
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

    public boolean isClientSide() {
        if (clientSide != null) {
            return clientSide.booleanValue();
        }
        ValueBinding vb = getValueBinding("clientSide");
        return vb != null ? ((Boolean) vb.getValue(getFacesContext())).booleanValue() : false;
    }       
    
    public void setPartialSubmit(boolean partialSubmit) {
        this.partialSubmit = new Boolean(partialSubmit);
    }

    public boolean isPartialSubmit() {
        if (partialSubmit != null) {
            return partialSubmit.booleanValue();
        }
        ValueBinding vb = getValueBinding("partialSubmit");
        return vb != null ? ((Boolean) vb.getValue(getFacesContext())).booleanValue() : false;
    }  
    
    public void encodeBegin(FacesContext context) throws IOException {
        //load js.js, will be loaded once per view. It handled by the ICEFaces framework.
        loadJs();
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
    
    public void setOnupdate(String onupdate) {
        this.onupdate = onupdate;
    }

    public String getOnupdate() {
        if (onupdate != null) {
            return onupdate;
        }
        ValueBinding vb = getValueBinding("onupdate");
        return vb != null ? ((String) vb.getValue(getFacesContext())) : null;
    }   
    
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getStyleClass() {
        if (styleClass != null) {
            return styleClass;
        }
        ValueBinding vb = getValueBinding("styleClass");
        return vb != null ? ((String) vb.getValue(getFacesContext())) : null;
    } 
    
    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyle() {
        if (style != null) {
            return style;
        }
        ValueBinding vb = getValueBinding("style");
        return vb != null ? ((String) vb.getValue(getFacesContext())) : null;
    }

    public MethodBinding getTabChangeListener() {
        return tabChangeListener;
    }

    public void setTabChangeListener(MethodBinding tabChangeListener) {
        this.tabChangeListener = tabChangeListener;
    }       
}

class TabSetJarResource extends JarResource {

    public TabSetJarResource(String path) {
        super(path);
    }
    
    public void withOptions(Options options) throws IOException {
    
    }
}