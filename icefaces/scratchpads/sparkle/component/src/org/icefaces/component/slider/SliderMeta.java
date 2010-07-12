package org.icefaces.component.slider;

import javax.el.MethodExpression;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

// Each Meta class requires a @Component annotation
@Component(
        // The tag name, as it will be used in view definitions (.xhtml files)
        tagName ="slider",
        // The end class that will be used by applications. Hand-coded.
        // The componentClass extends generatedClass, which extends extendsClass.
        componentClass ="org.icefaces.component.slider.Slider",
        // The renderer, which outputs the html markup and javascript. Hand-coded. 
        rendererClass ="org.icefaces.component.slider.SliderRenderer",
        // Generated, to contain all of the properties, getters, setters, and
        //  state saving methods. 
        generatedClass = "org.icefaces.component.slider.SliderBase",
        // The super-class of the component. Did not extend UIInput, because
        //  none of the conversion nor validation facilities it provides are
        //  relevant to a slider.
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icesoft.faces.Slider",
        rendererType = "org.icesoft.faces.SliderRenderer",
        componentFamily="com.icesoft.faces.Slider",
        tlddoc="This is an extension of YUI's slider component."
    )
public class SliderMeta {
    // Every java field in the Meta class can be annotated to become either a
    //  Property, Field or Facet, in the generatedClass.
    // @Property annotations are for properties that should have a StateHelper,
    //  getter method, setter method, and state saving code generated for it.
    //  It should be a public property for the component tag.
    // @Field annotations are for fields that are internal to the component,
    //  to maintain state through-out a lifecycle, or even between lifecycles,
    //  if it is not transient, and so will participate in state saving.
    // @Facet annotations, on fields, provide a means of describing and
    //  documenting component facets, in a standard way. @Facets annotations,
    //  on inner classes, provides a means of using a facet specific namespace
    //  that will not collide with samely named properties or fields.
    
    @Property (defaultValue="x", 
            tlddoc="It could be either x, for horizontal or y, for vertical. Default value is set to x. Pass through to YUI component")
    private String axis;
    
    @Property (defaultValue="0", 
            tlddoc="The minimum value of slider, default is 0. Pass through to YUI component")
    private Integer min;
    
    @Property (defaultValue="100",
            tlddoc="The maximum value of slider, default is 100. Pass through to YUI component")
    private Integer max;
    
    @Property (defaultValue="0",
            tlddoc="The value of slider, default is 0. Pass through to YUI component")
    private Integer value;    
    
    @Property (defaultValue="150px",
            tlddoc="The length of slider, default is 150px. Pass through to YUI component")
    private String length;
    
    @Property (defaultValue="slideEnd",
            tlddoc="The 3 valid values are slideStart, slideEnd and slideInterval. " +
            		"Default value is slideEnd",
            javadocGet="returns value of submitOn. It is not a pass through because slideInterval is a custom event " +
            		"which internally uses \"thumbMove\"")
    private String submitOn;   
    
    @Property (defaultValue="500",
            tlddoc="This attribute has an effect only when \"submitOn\" is set to \"slideInterval\". Default value is 500 (milliseconds)")
    private Integer slideInterval; 
    
    @Property(defaultValue="false",
            tlddoc="Default is false, means uses full submit.")
    private Boolean singleSubmit;
    
    @Property(tlddoc="style of the component")
    private String style;
    
    @Property(tlddoc="style class of the component, the renderer doesn't render any default class.")
    private String styleClass;  

    @Property()
    private String thumbUrl; 

    @Property (defaultValue="0", tlddoc="tabindex of the component")
    private Integer tabindex;  
    
    @Property(defaultValue="false")
    private Boolean immediate;
	
    @Property(defaultValue="true")
    private Boolean clickableRail;
    
    // A MethodExpression Property is a special type, that does not generate
    //  the same code, as it does not use a ValueExpression, but instead
    //  describes a method to be called, and the parameter to pass to it.
    @Property(isMethodExpression=true, methodExpressionArgument="javax.faces.event.ValueChangeEvent")
    private MethodExpression valueChangeListener;
}
