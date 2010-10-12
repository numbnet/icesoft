package org.icefaces.component.tab;

import javax.faces.component.UIComponent;

import org.icefaces.component.annotation.*;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.component.baseMeta.UICommandMeta;

@Component(tagName ="tab",
        componentClass ="org.icefaces.component.tab.Tab",
        componentType = "com.icesoft.faces.Tab",
        extendsClass = "javax.faces.component.UICommand", 
        generatedClass = "org.icefaces.component.tab.TabBase",
        componentFamily="com.icesoft.faces.TabSet"
        )
public class TabMeta extends UICommandMeta {
    @Property (tlddoc="This attribute represents Label of the tab")  
    private String label;
    @Property (tlddoc="If true then this tab will be disabled and can not be selected") 
    private Boolean disabled;    
    @Property(implementation=Implementation.EXISTS_IN_SUPERCLASS)   
    private String id;    
    @Property(implementation=Implementation.EXISTS_IN_SUPERCLASS)   
    private Boolean rendered;    

    
    @Facets
    class FacetsMeta{
        @Facet
        UIComponent header;
        @Facet
        UIComponent body;
        @Facet
        UIComponent footer;    
        @Facet
        UIComponent label;          
    }    
}