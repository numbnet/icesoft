package org.icefaces.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
  String EMPTY = "";
  String tag_name();
  String extends_class();
  String renderer_class()default EMPTY;
  String component_class();
  String generated_class()default EMPTY;
  String renderer_type();
  String component_type();
  String component_family() default EMPTY;
  String base_tagclass() default "javax.faces.webapp.UIComponentELTag";
  String handler_class() default "com.icesoft.faces.component.facelets.IceComponentHandler";
  String javadoc() default "";
  String tlddoc() default "";  
  String[] includeProperties() default {};
  String[] excludeProperties() default {};
}
