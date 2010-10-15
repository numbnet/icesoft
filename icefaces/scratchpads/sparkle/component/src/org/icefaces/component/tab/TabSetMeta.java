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

import org.icefaces.component.baseMeta.UIComponentBaseMeta;

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
	@ResourceDependency(name="yui/yui-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="loader/loader-min.js",library="yui/3_1_1"),
    @ResourceDependency(name ="anim/anim-min.js",library = "yui/3_1_1"),
    @ResourceDependency(name ="plugin/plugin-min.js",library = "yui/3_1_1"),    
    @ResourceDependency(name ="pluginhost/pluginhost-min.js",library = "yui/3_1_1"),      
    @ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),    
    @ResourceDependency(name="yui3.js",library="org.icefaces.component.util"),   
    @ResourceDependency(name="animation.js",library="org.icefaces.component.animation"),
    @ResourceDependency(name="animation.css",library="org.icefaces.component.animation") ,	
	@ResourceDependency(name="2_8_1/fonts/fonts-min.css",library="yui"),
	@ResourceDependency(name="2_8_1/yahoo-dom-event/yahoo-dom-event.js",library="yui"),
	@ResourceDependency(name="2_8_1/connection/connection-min.js",library="yui"),
	@ResourceDependency(name="2_8_1/element/element-min.js",library="yui"),
	@ResourceDependency(name="2_8_1/tabview/tabview-min.js",library="yui"),
    @ResourceDependency(name="tabset.js",library="org.icefaces.component.tab"),
    @ResourceDependency(name="tabset.css",library="org.icefaces.component.tab")    
})
@ClientBehaviorHolder 
public class TabSetMeta extends UIComponentBaseMeta {
    
    @Property(defaultValue="false", tlddoc="The default value of this attribute is fals. If true then tab change event will happen in APPLY_REQUEST_VALUES phase and if the value of this attribute is false then event change will happen in INVOKE_APPLICATION phase")    
    private boolean immediate; 
    
    @Property(defaultValue="0", tlddoc="This attribute represents index of the current selected tab")
    private int selectedIndex;
    
    @Property(defaultValue="top", tlddoc="This attribute represents orientation of tabs. Valid values are bottom, top, left and right")   
    private String orientation;
    
    @Property(defaultValue="false", tlddoc="This component supports both client and server side tab change modal. When this attribute is set to true, then contents of all tabs gets rendered on client and tabchange would also occur on client. If this attribute is set to false which is default then only current selected tab will get rendered to the client and tab change request will goto server to render requested tab, which allows to send dynamic contents back.")       
    private boolean clientSide; 
   
    @Property(defaultValue="false", tlddoc="The default value of this attribute is false, so in this case full submit is being used, where all component gets rendered and executed. If this attribute is set to true, then only this component gets executed and entire view gets rendered")
    private boolean singleSubmit;
    
    @Property(defaultValue="true", tlddoc="This attribute comes into effect when there is a validation error. By default it is set to true, which means that if on a tab change there is a validation error, that error will be ignored and tab will be changed successfully and if this attribute is set to false then on a validation error tab will not be changed untill validation error gone.") 
    private boolean cancelOnInvalid;    
    
    @Property (tlddoc="style class will be rendered on a root element of this component")
    private String styleClass;
    
    @Property (tlddoc="style will be rendered on a root element of this component") 
    private String style;
    
    @Property(expression=Expression.METHOD_EXPRESSION, methodExpressionArgument="javax.faces.event.ValueChangeEvent",
            tlddoc="on tabchange value change event can be captured using this listener")
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
