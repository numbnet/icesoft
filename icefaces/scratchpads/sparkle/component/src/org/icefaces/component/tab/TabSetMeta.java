package org.icefaces.component.tab;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.component.annotation.*;
@Component(
    tagName ="tabPanel",
    componentClass ="org.icefaces.component.tab.TabSet",
    rendererClass ="org.icefaces.component.tab.TabSetRenderer",
    componentType = "org.icesoft.faces.TabSet",
    rendererType = "org.icesoft.faces.TabSetRenderer",
    extendsClass = "javax.faces.component.UIComponentBase",
    generatedClass = "org.icefaces.component.tab.TabSetBase",
    componentFamily="com.icesoft.faces.TabSet"    
)
@ResourceDependencies({
    @ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),    
    @ResourceDependency(name="tabset.js",library="org.icefaces.component.tab"),
    @ResourceDependency(name="tabset.css",library="org.icefaces.component.tab")    
})
@ClientBehaviorHolder 
public class TabSetMeta {
    
    @Property(defaultValue="false")    
    private Boolean immediate; 
    
    @Property(defaultValue="0", tlddoc="index of the tabset")
    private Integer selectedIndex;
    
    @Property(defaultValue="top", tlddoc="valid values are bottom, top, left and right")   
    private String orientation;
    
    @Property(defaultValue="false", tlddoc="default value is false, if true, all tabs and its contents will be render on client as well as tab change will happen on clinet. There will be no communication with server")       
    private Boolean clientSide; 
   
    @Property(defaultValue="false", tlddoc="default value is false, so in the case full submit will be use, where all component will be executed and rendered if true, then only this component will be executed and entire view will get rendered")
    private Boolean singleSubmit;
    
    @Property(defaultValue="true") 
    private Boolean cancelOnInvalid;    
    
    @Property (tlddoc="Allows to register client side callback that will be executed on every render cycle")   
    private String onupdate;
    
    @Property (tlddoc="style class will be rendered on a root element of this component")
    private String styleClass;
    
    @Property (tlddoc="style will be rendered on a root element of this component") 
    private String style;
    
    @Property(isMethodExpression=Expression.METHOD_EXPRESSION, methodExpressionArgument="javax.faces.event.ValueChangeEvent",
            tlddoc="on tabchange value change event can be captured using this listener")
    private MethodExpression tabChangeListener;
    
    @Property(defaultValue="false") 
    private Boolean effectOnHover;  
    
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
