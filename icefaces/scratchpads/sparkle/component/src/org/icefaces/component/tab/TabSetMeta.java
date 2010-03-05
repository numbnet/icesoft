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
import org.icefaces.component.annotation.Field;
import org.icefaces.component.annotation.Property;

@Component(
    tagName ="tabSet",
    componentClass ="org.icefaces.component.tab.TabSet",
    rendererClass ="org.icefaces.component.tab.TabSetRenderer",
    componentType = "org.icesoft.faces.TabSet",
    rendererType = "org.icesoft.faces.TabSetRenderer",
    extendsClass = "javax.faces.component.UIComponentBase",
    generatedClass = "org.icefaces.component.tab.TabSetBase",
    componentFamily="com.icesoft.faces.TabSet"    
)
public class TabSetMeta {
    @Field
    protected Integer submittedTabIndex;
    
    @Field
    private String oldOrientation; 
    
    @Property(defaultValue="false")    
    private Boolean immediate; 
    
    @Property(defaultValue="0")
    private Integer tabIndex;
    
    @Property(defaultValue="top")   
    private String orientation;
    
    @Property(defaultValue="false")    
    private Boolean clientSide; 
   
    @Property(defaultValue="false") 
    private Boolean singleSubmit;
    
    @Property(defaultValue="true") 
    private Boolean cancelOnInvalid;    
    
    @Property    
    private String onupdate;
    
    @Property
    private String styleClass;
    
    @Property (defaultValue="") 
    private String style;
    
    @Property(isMethodExpression=true, methodExpressionArgument="javax.faces.event.ValueChangeEvent")
    private MethodExpression tabChangeListener;
    
    
    @Facets
    class FacetsMeta{
        @Facet
        UIComponent header;
        @Facet
        UIComponent body;
        @Facet
        UIComponent footer;           
    }

}
