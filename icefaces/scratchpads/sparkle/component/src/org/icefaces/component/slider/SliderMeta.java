package org.icefaces.component.slider;

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

    @Property
    private Integer axis;
    
    @Property
    private Integer min;
    
    @Property
    private Integer max;
    
    @Property
    private Integer value;    
    
    @Property
    private String railSize;
    

}
