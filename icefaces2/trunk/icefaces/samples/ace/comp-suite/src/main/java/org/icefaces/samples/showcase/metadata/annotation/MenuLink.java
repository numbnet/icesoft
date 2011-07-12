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
public @interface MenuLink {

    String title();
    boolean isDefault() default false;
    boolean isDisabled() default false;
    boolean isNew() default false;
    String exampleBeanName();
}
