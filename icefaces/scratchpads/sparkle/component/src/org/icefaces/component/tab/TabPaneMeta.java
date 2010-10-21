package org.icefaces.component.tab;

import javax.faces.component.UIComponent;

import org.icefaces.component.annotation.*;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.component.baseMeta.UICommandMeta;

@Component(tagName = "tabPane",
        componentClass  = "org.icefaces.component.tab.TabPane",
        generatedClass  = "org.icefaces.component.tab.TabPaneBase",
        extendsClass    = "javax.faces.component.UIComponentBase", 
        componentType   = "org.icefaces.TabPane",
        componentFamily = "org.icefaces.TabPane")
public class TabPaneMeta extends UICommandMeta {
    @Property (tlddoc="This attribute represents Label of the tab")  
    private String label;
    
    @Property (tlddoc="If true then this tab will be disabled and can not be selected") 
    private boolean disabled;    

    
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