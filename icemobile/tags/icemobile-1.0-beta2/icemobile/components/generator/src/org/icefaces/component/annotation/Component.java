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
 * The component annotation takes information, which is used by the tld, faces-config,
 * faces-taglib as well as the component itself. It has some mandatory fields and some
 * that are optional. Which allows to specify following information:
 * <ul>
 * <li> the name and location of the class to be generated</li>
 * <li> component type</li>
 * <li> renderer type</li>
 * <li> renderer class </li>
 * <li> tag name </li>
 * <li> Javadoc</li>
 * <li> TLD doc</li>
 * </ul>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
    final String EMPTY = "";

    /**
     * Name of tag. Its a mandatory field.
     *
     * @return defined tag name.
     */
    String tagName();

    /**
     * Class that has to be extended by the generated component. Its a mandatory field.
     *
     * @return fully qualified name of the class has to be extended.
     */
    String extendsClass();

    /**
     * fully qualified name of the class of the Renderer, use by the target component.
     * (This class has to be created by developer)
     *
     * @return fully qualified name of the Renderer class.
     */
    String rendererClass() default EMPTY;

    /**
     * fully qualified name for the component class to be generated. Its a mandatory field.
     *
     * @return fully qualified name of the component class.
     */
    String componentClass();

    /**
     * by default generated classes are leaf classes, so you can't override any behaviour.
     * If you want to hand code the leaf class and extend the generated one then you can
     * use this attribute in conjunction with componentClass attribute. For example:
     * HandCodedClass --> GeneratedClass --> UIComponent
     *
     * @return fully qualified name of the generated class.
     */
    String generatedClass() default EMPTY;

    /**
     * renderer type
     *
     * @return type of the renderer
     */
    String rendererType() default EMPTY;

    /**
     * type of the component.
     *
     * @return component type.
     */
    String componentType();

    /**
     * name of the component family.
     *
     * @return component family.
     */
    String componentFamily() default EMPTY;

    /**
     * defines a base tag class that can be extended by the generated tag class. default is
     * "javax.faces.webapp.UIComponentELTag".
     *
     * @return fully qualified name of base tag class.
     */
    String baseTagClass() default "javax.faces.webapp.UIComponentELTag";

    /**
     * facelets handler class. default is "com.icesoft.faces.component.facelets.IceComponentHandler".
     * Developer don't have to override it, generator takes care of it. It is just in case if
     * developer want to use its own handler class.
     *
     * @return facelets handler class.
     */
    String handlerClass() default "";

    /**
     * javadocs for the component class. Goes into the component class.
     *
     * @return javadocs for the component class.
     */
    String javadoc() default EMPTY;

    /**
     * tld docs for the component class. Goes into the Tld documents.
     *
     * @return component doc for tld.
     */
    String tlddoc() default EMPTY;

    /**
     * Name of the properties, that needs to be included from the parent class.
     *
     * @return property names.
     */
    String[] includeProperties() default {};

    //
    //	Name of the properties to be excluded from superclasses.
    //	@return property names.
    //
    //String[] disinheritProperties() default {};
}
