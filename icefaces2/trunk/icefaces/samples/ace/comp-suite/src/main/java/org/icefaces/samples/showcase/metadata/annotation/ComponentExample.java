package org.icefaces.samples.showcase.metadata.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentExample {
    final String EMPTY = "";
//      todo, likely don't need to annotate these properties as they are view specific
    String parent() default EMPTY;

    String title() default EMPTY;

    String description() default EMPTY;

    String example();
}
