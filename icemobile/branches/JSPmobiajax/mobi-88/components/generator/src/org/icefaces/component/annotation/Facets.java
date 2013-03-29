/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
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
 *     @Property
 * 	private Integer currentIndex;
 *
 * @Facets class FacetsMeta{
 * @Facet UIComponent header;
 * <p/>
 * @Facet UIComponent body;
 * <p/>
 * @Facet (javadocGet="returns footer facet", javadocSet="Sets footer facet")
 * UIComponent footer;
 * }
 * }
 * <p/>
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Facets {

}
