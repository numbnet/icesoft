package org.icefaces.samples.showcase.metadata.annotation;

import java.lang.annotation.*;

/**
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExampleResources {
    ExampleResource[] resources() default {};
}
