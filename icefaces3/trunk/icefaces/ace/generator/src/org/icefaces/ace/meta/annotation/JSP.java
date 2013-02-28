/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.ace.meta.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to generate pure JSP (non-JSF) Tag file, TLD, TLDDOC, Javadoc.
 * Specify this annotation on Meta classes, optionally along-side any
 * \@Component annotation. Use the @Property annotation on the Meta
 * class' fields, and discern between properties that are only for
 * JSP or only for JSF via the @Only annotation.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSP {
    final String EMPTY = "";
    
    /**
     * Name of tag. If not specified, then it defaults to the Meta class'
     * simple name, without the "Meta" suffix, using camel case.
     * Eg: MyCompMeta -> myComp
     * @return defined tag name.
     */
    String tagName() default EMPTY;

    /**
     * fully qualified name for the tag class. If not specified, then it
     * defaults to the Meta class' full name, without the "Meta" suffix,
     * but with a "Tag" suffix added.
     * Eg: org.mypackage.MyCompMeta -> org.mypackage.MyCompTag
     * @return fully qualified name of the tag class.
     */
    String tagClass() default EMPTY;

    /**
     * By default, generated classes are leaf classes, so you can't override any
     * behaviour. If you want to hand code the tag class, and have it
     * extend the generated one then you can use this attribute in conjunction
     * with tagClass attribute. So, if generatedClass is specified:
     * (manual) tagClass extends generatedClass extends extendsClass.
     * Eg: org.mypackage.MyCompBaseTag
     * Otherwise: (generated) tagClass extends extendsClass.
     * @return fully qualified name of the generated class.
     */
    String generatedClass() default EMPTY;

    /**
     * Class that is to be extended by the generated tag. It's a mandatory field.
     * @return fully qualified name of the class has to be extended.
     */
    String extendsClass();

    /**
     * tld doc for the tag class. Goes into the Tld documentation.
     * @return tag documentation for tld.
     */
    String tlddoc() default EMPTY;

    /**
     * javadoc for the tag class. Goes into the generated tag class.
     * If not specified, defaults to being the same as tlddoc.
     * @return javadoc for the generated tag class.
     */
    String javadoc() default EMPTY;
}
