package com.icesoft.faces.component.generator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyDuo {
    boolean isMethodExpression() default false;
    boolean isTransient() default false;
    String default_value() default "false";
    String name() default "";
    String type() default "";
    String javadocSet() default "";
    String javadocGet() default "";
    String tlddoc() default "";
    //tld requires this info
    boolean required() default false;
}
