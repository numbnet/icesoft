package org.icefaces.render;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  ExternalScript annotation allows an external script declaration to be loaded
 * into the head portion of the page. If a second argument is specified,
 * it identifies a context param which acts as a check gate for this parameter.  
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExternalScript {
    final String Null = "Null";
    
    String contextParam() default Null;
    String scriptURL();
}
