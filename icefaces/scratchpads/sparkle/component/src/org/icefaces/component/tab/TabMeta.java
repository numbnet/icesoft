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
    @Property   
    private String label;
    @Property   
    private String dataSrc;
    @Property   
    private Boolean cacheData;
    @Property   
    private Boolean disabled;    
    @Property(inherit=Inherit.SUPERCLASS_PROPERTY)   
    private String id;    
    @Property(inherit=Inherit.SUPERCLASS_PROPERTY)   
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