package org.icefaces.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows to add facet support to the generated component. It can be used in conjunction 
 * with @Facets annotation.
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Facet {
	final String EMPTY = "";
	
	/**
	 * Name of the facet, if not defined one the property name will be used instead.
	 * @return facet name.
	 */
    String name() default EMPTY;
    
    /**
     * Java doc for generated getter
     * @return getter javadoc
     */
    String javadocGet() default EMPTY;
    
    /**
     * Javadoc for generated setter
     * @return setter javadoc
     */
    String javadocSet() default EMPTY;
}
