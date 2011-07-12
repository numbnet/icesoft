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
public @interface ExampleResource {
    final String EMPTY = "";

    public String title();

    public String resource();

    public ResourceType type() default ResourceType.java;
}
