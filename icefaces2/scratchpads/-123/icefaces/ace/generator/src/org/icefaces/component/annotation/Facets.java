/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

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
