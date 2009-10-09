package org.icefaces.component.tab;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

@Component(
    tag_name="tabSet",
    component_class="org.icefaces.component.tab.TabSet",
    renderer_class="org.icefaces.component.tab.TabSetRenderer",
    component_type = "org.icesoft.faces.TabSet",
    renderer_type = "org.icesoft.faces.TabSetRenderer",
    extends_class = "javax.faces.component.UICommand",
    generated_class = "org.icefaces.component.tab.TabSetBase")
public class TabSetMeta {
    @Property    
    protected Boolean clientSide; 
   
    @Property    
    protected Boolean partialSubmit;
    
    @Property    
    protected String onupdate;
    
    @Property
    protected String styleClass;
    
    @Property    
    protected String style;
}