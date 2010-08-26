package org.icefaces.component.tab;

import javax.faces.component.UIComponent;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Facet;
import org.icefaces.component.annotation.Facets;
import org.icefaces.component.annotation.Properties;
import org.icefaces.component.annotation.Property;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(tagName ="tab",
        componentClass ="org.icefaces.component.tab.Tab",
        componentType = "com.icesoft.faces.Tab",
        extendsClass = "javax.faces.component.UICommand", 
        generatedClass = "org.icefaces.component.tab.TabBase",
        componentFamily="com.icesoft.faces.TabSet"
        )
public class TabMeta {
    @Property   
    private String label;
    @Property   
    private String dataSrc;
    @Property   
    private Boolean cacheData;
    @Property   
    private Boolean disabled;    
    @Property(inherit=true)   
    private String id;    
    @Property(inherit=true)   
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