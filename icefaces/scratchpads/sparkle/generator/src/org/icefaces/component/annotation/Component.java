package org.icefaces.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
  final String EMPTY = "";
  String tagName();
  String extendsClass();
  String rendererClass()default EMPTY;
  String componentClass();
  String generatedClass()default EMPTY;
  String rendererType()default EMPTY;
  String componentType();
  String componentFamily() default EMPTY;
  String baseTagClass() default "javax.faces.webapp.UIComponentELTag";
  String handlerClass() default "com.icesoft.faces.component.facelets.IceComponentHandler";
  String javadoc() default "";
  String tlddoc() default "";  
  String[] includeProperties() default {};
}
