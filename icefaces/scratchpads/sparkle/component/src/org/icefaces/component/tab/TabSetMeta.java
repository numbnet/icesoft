package org.icefaces.component.tab;

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

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Facet;
import org.icefaces.component.annotation.Facets;
import org.icefaces.component.annotation.Property;

@Component(
    tagName ="tabSet",
    componentClass ="org.icefaces.component.tab.TabSet",
    rendererClass ="org.icefaces.component.tab.TabSetRenderer",
    componentType = "org.icesoft.faces.TabSet",
    rendererType = "org.icesoft.faces.TabSetRenderer",
    extendsClass = "javax.faces.component.UICommand",
    generatedClass = "org.icefaces.component.tab.TabSetBase")
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
    
    @Property
    protected MethodExpression tabChangeListener;
    
    @Facets
    class MetaFacets {
        @Facet
        UIComponent header;
        @Facet
        UIComponent body;
        @Facet
        UIComponent footer;           
    }

}
