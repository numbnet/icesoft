package org.icefaces.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
    boolean isMethodExpression() default false;
    boolean isTransient() default false;
    String defaultValue() default "false";
    String name() default "";
    String type() default "";
    String tlddoc() default "";
    String javadocGet() default "";    
    String javadocSet() default "";
    //tld requires this info
    boolean required() default false;
    boolean useTemplate() default false;
    boolean inherit() default false;
}
