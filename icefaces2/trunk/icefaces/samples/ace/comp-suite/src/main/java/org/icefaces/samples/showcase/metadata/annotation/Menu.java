package org.icefaces.samples.showcase.metadata.annotation;

import java.lang.annotation.*;

/**
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Menu {
   String title();
   MenuLink[] menuLinks() default {};
}
