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
public @interface Application {

    // default menu to display, ace, compat, ee etc.
    Class<? extends org.icefaces.samples.showcase.metadata.context.Menu> defaultNavigation();
}
