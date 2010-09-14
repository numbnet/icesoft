package org.icefaces.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Property annotation allows to add JSF managed properties to the generated component. All
 * fields are optional on this annotation.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
    final String Null = "null";
    
    /**
     * if generated property is a method expression, then this field can be set to true. (e.g.)
	 * <pre>
	 * @Property(isMethodExpression=true, methodExpressionArgument="javax.faces.event.ValueChangeEvent")
	 * public MethodExpression valueChangeListener;
	 * </pre>
     * @return boolean value.
     */
    Expression expression() default Expression.UNSET;
    
    /**
     * Allows to define method expression argument if any.
     * @return fully qualified name of the argument class.
     */
    String methodExpressionArgument() default Null;
    
    /**
     * Default value of the property.
     * @return default value.
     */
    String defaultValue() default Null;
    
    /**
     * By default the value being assigned to the property as string  literal (e.g.)
     * <pre>
     * @Property (value="Car")
     * String type;
     * 
     * The generated property would look something like this:
     * 
     * String type = "Car";
     * 
     * But what if you want to define some other type then string or a constant, expression etc. You don't want
     * value to be quoted in that case. So you would set this attribute to false. (e.g)
     * 
     * @Property (value="10", defaultValueIsStringLiteral=false)
     * Integer count;      
     * </pre>
     * @return 
     */    
    DefaultValueType defaultValueType() default DefaultValueType.UNSET;
    
    
    /**
     * TLDDoc for this property
     * @return property tlddoc
     */
    String tlddoc() default Null;
    
    /**
     * javadoc for the getter.
     * @return getter javadoc.
     */    
    String javadocGet() default Null;  
    
    /**
     * javadoc for the setter.
     * @return setter javadoc.
     */     
    String javadocSet() default Null;
    
    /**
     * Attribute that goes inside the TLD for each attribute. It also helps IDEs
     * 
     */
    Required required() default Required.UNSET;
    
    /**
     * the "org.icefaces.component.annotation.PropertyTemplate" class keeps template for 
     * commonly used properties. Which usually defines javadoc for properties. The PropertyTemplate
     * can be used by any property annotation by just setting this filed to true.
     * @return boolean value
     */
    boolean useTemplate() default false;
    
    /**
     * Only those attribute goes inside the tag class which are defined in 
     * Meta class, so if you have to expose some of the parent class attribute in tag 
     * class then you can set this property to true. inherit true tells generator to 
     * create setter in tag class as well as add the attribute info in TLD for tlddocs 
     * and IDEs
     * @return
     */
    Inherit inherit() default Inherit.UNSET;
}
