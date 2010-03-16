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
    
    @Property (defaultValue="String.valueOf(getClientId().hashCode())", 
        defaultValueIsStringLiteral=false)
    private String varName;

    @Property (defaultValue="x")
    private String axis;
    
    @Property (defaultValue="0")
    private Integer min;
    
    @Property (defaultValue="100")
    private Integer max;
    
    @Property (defaultValue="0")
    private Integer value;    
    
    @Property (defaultValue="200px")
    private String railSize;
    
    @Property (defaultValue="slideEnd")
    private String submitOn;   
    
    @Property(defaultValue="false") 
    private Boolean singleSubmit;
    
    @Property
    private String style;
    
    @Property
    private String styleClass;  

    @Property
    private String thumbImage; 

    @Property (defaultValue="500")
    private Integer sliderInterval; 

    
    
    @Property(isMethodExpression=true, methodExpressionArgument="javax.faces.event.ValueChangeEvent")
    private MethodExpression valueChangeListener;
}
