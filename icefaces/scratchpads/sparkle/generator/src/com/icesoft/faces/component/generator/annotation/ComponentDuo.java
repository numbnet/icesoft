package com.icesoft.faces.component.generator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentDuo {
  String EMPTY = "";
  String tag_name();
  String renderer_class()default EMPTY;
  String component_class()default "javax.faces.webapp.UIComponentELTag";
  String base_tagclass() default "javax.faces.webapp.UIComponentELTag";
  String handler_class() default "com.icesoft.faces.component.facelets.IceComponentHandler";
  String javadoc() default "";
  PropertyDuo[] inheritedProperties() default {};
  PropertyDuo[] avoidInheriting() default {};  
}
