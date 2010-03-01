package org.icefaces.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {
    boolean isTransient() default false;
    String defaultValue() default "null";
    String javadoc() default ""; 
    boolean defaultValueIsStringLiteral() default true;
}
