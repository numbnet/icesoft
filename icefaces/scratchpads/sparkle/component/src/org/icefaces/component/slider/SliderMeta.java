package org.icefaces.component.slider;

import javax.el.MethodExpression;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

@Component(
        tagName ="slider",
        componentClass ="org.icefaces.component.slider.Slider",
        rendererClass ="org.icefaces.component.slider.SliderRenderer",
        componentType = "org.icesoft.faces.Slider",
        rendererType = "org.icesoft.faces.SliderRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        generatedClass = "org.icefaces.component.slider.SliderBase",
        componentFamily="com.icesoft.faces.Slider"    
    )
public class SliderMeta {
    
    @Property (defaultValue="x", 
            tlddoc="It could be either x or y. default value is set to x. Pass trhough to YUI component")
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
    
    @Property (defaultValue="200px",
            tlddoc="The railSize of slider, default is 200px. Pass through to YUI component")
    private String railSize;
    
    @Property (defaultValue="slideEnd",
            tlddoc="The 3 valid values are slideStart, slideEnd and slideInterval, " +
            		"default value is slideEnd",
            javadocGet="returns value of submitOn. Its not a pass throug because sliderInterval is a custom event " +
            		"which internally uses \"thumbDrag\"")
    private String submitOn;   
    
    @Property (defaultValue="500",
            tlddoc="This attribute has an effect only when \"submitOn\" is set to \"sliderInterval\". default value is 500 (mili seconds)")
    private Integer slideInterval; 
    
    @Property(defaultValue="false",
            tlddoc="Default is false, means uses full submit.")
    private Boolean singleSubmit;
    
    @Property(tlddoc="style of the component")
    private String style;
    
    @Property(tlddoc="style class of the component, the renderer doesn't render any default class.")
    private String styleClass;  

    @Property(defaultValue="http://yui.yahooapis.com/3.0.0/build/slider/assets/skins/sam/thumb-classic-x.png")
    private String thumbImage; 

    @Property (defaultValue="0", tlddoc="tabindex of the component")
    private Integer tabindex;  
    
    @Property(isMethodExpression=true, methodExpressionArgument="javax.faces.event.ValueChangeEvent")
    private MethodExpression valueChangeListener;
}
