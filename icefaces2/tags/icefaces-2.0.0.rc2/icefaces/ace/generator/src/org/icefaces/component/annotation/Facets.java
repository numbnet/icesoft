package org.icefaces.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows to add facet support to the component. The @Facets annotation must be defined in 
 * an inner class (e.g.)
 * <pre>
 * public class TabSetMeta {
 *	@Property
 *	private Integer currentIndex;

	*@Facets
	*class FacetsMeta{
	*	@Facet		
    *    UIComponent header;

    *	@Facet		
*       UIComponent body;

*		@Facet (javadocGet="returns footer facet", javadocSet="Sets footer facet")
 *               UIComponent footer;           	
 *       }
 * }
 * 
 * </pre>
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Facets {

}
