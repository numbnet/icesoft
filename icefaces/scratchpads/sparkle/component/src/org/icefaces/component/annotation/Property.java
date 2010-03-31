package org.icefaces.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
    final String Null = "null";
    boolean isMethodExpression() default false;
    String methodExpressionArgument() default Null;
    String defaultValue() default Null;
    String tlddoc() default Null;
    String javadocGet() default Null;    
    String javadocSet() default Null;
    //tld requires this info
    boolean required() default false;
    boolean useTemplate() default false;
    boolean inherit() default false;
    boolean defaultValueIsStringLiteral() default true;
}
