  package org.icefaces.component.testComponent;

import javax.faces.component.UIComponent;
import javax.el.MethodExpression;

import org.icefaces.component.annotation.*;

  @Component(
          tagName ="testComponent",
          componentClass ="org.icefaces.component.testComponent.TestComponent",
          rendererClass ="org.icefaces.component.testComponent.TestComponentRenderer",
          componentType = "org.icefaces.TestComponent",
          rendererType = "org.icefaces.TestComponentRenderer",
          extendsClass = "javax.faces.component.UICommand",
          generatedClass = "org.icefaces.component.testComponent.TestComponentBase",
          componentFamily="com.icesoft.faces.TestComponent"
  )

  /**
   * The intention is to insert test properties here to fully exercise
   * the code generation portion of the component generator.
   */
  public class TestComponentMeta {

      @Property
      private String stringPropertyOne;

      @Property (defaultValue="default String Value")
      private String stringPropertyTwo;

      @Property (defaultValue="21")
      private String stringPropertyThree;

      @Property(defaultValue="false",inherit=Inherit.SUPERCLASS_PROPERTY, useTemplate=true)
      private Boolean immediate;

      @Property (inherit=Inherit.SUPERCLASS_PROPERTY, useTemplate=true)
      private String id;

      // boolean

      @Property
      private Boolean BooleanWrapperOne;

      @Property (defaultValue="true")
      private Boolean BooleanWrapperTwo;

       @Property
      private boolean BooleanOne;

      @Property (defaultValue="true")
      private boolean BooleanTwo;

      @Property
      private String label;


      // Integer

      @Property
      private int integerOne;

      @Property (defaultValue="12")
      private int integerTwo;

      @Property
      private Integer IntegerWrapperOne;

      @Property (defaultValue="12")
      private Integer IntegerWrapperTwo;

      // floats

      @Property
      private float floatOne;

      @Property (defaultValue="12f")
      private float floatTwo;

      @Property
      private Float FloatWrapperOne;

      @Property (defaultValue="12.345678f")
      private Float FloatWrapperTwo;

      // long
      @Property
      private long longOne;

      @Property (defaultValue="12l")
      private long longTwo;

      @Property
      private Long LongWrapperOne;

      @Property (defaultValue="12L")
      private Long LongWrapperTwo;

      @Property
      private String stringOne;

      @Property  (defaultValue="Hello World!")
      private String stringTwo;

      @Property (inherit=Inherit.SUPERCLASS_PROPERTY, useTemplate=true)
      private UIComponent binding;

      @Property (defaultValue="false")
      private Boolean disabled;

      @Property (inherit=Inherit.SUPERCLASS_PROPERTY, defaultValue="true")
      private Boolean rendered;

      @Property (defaultValue="0", tlddoc="tabindex of the component")
      private Integer tabindex;

      @Property(tlddoc="style class of the component, the renderer doesn't render any default class.")
      private String styleClass;

      @Property(tlddoc="style of the component", defaultValue="text-decoration: XXXXXX-YYY;")
      private String style;

      @Property(inherit=Inherit.SUPERCLASS_PROPERTY, isMethodExpression=Expression.METHOD_EXPRESSION)
      private MethodExpression actionListener;

      @Property(isMethodExpression=Expression.METHOD_EXPRESSION, inherit=Inherit.SUPERCLASS_PROPERTY	)
      private MethodExpression action;


  }