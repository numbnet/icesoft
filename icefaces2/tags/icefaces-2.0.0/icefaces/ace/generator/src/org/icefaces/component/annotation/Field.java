package org.icefaces.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allows to create a protected member for component internal use and 
 * by default adds it to the saveState/restoreState
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {
	/**
	 * Transient property will not be saved and restored.
	 * @return boolean value.
	 */
    boolean isTransient() default false;
    
    /**
     * Default value of the property.
     * @return default value.
     */
    String defaultValue() default "null";
    
    /**
     * javadoc for the property.
     * @return javadoc.
     */
    String javadoc() default ""; 
    
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
    boolean defaultValueIsStringLiteral() default true;
}
