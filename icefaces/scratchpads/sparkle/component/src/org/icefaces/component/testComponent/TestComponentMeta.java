  package org.icefaces.component.testComponent;

import javax.faces.component.UIComponent;
import javax.el.MethodExpression;

import org.icefaces.component.annotation.*;

import org.icefaces.component.baseMeta.UICommandMeta;

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
  public class TestComponentMeta extends UICommandMeta {

      @Property(defaultValue="false",inherit=Inherit.SUPERCLASS_PROPERTY)
      private Boolean immediate;

      @Property (inherit=Inherit.SUPERCLASS_PROPERTY)
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

      @Property (defaultValue="11111111")
      private int integerTwo;

      @Property
      private Integer integerWrapperOne;

      @Property (defaultValue="12345678")
      private Integer integerWrapperTwo;

      // floats

      @Property
      private float floatOne;

      @Property (defaultValue="1234.5678f")
      private float floatTwo;

      @Property
      private Float floatWrapperOne;

      @Property (defaultValue="1234.5678f")
      private Float floatWrapperTwo;

       @Property
      private float doubleOne;

      @Property (defaultValue="1234.5678")
      private double doubleTwo;

      @Property
      private Double doubleWrapperOne;

      @Property (defaultValue="1234.5678")
      private Double doubleWrapperTwo;

      // long
      @Property
      private long longOne;

      @Property (defaultValue="12345678l")
      private long longTwo;

      @Property
      private Long longWrapperOne;

      @Property (defaultValue="1234567l")
      private Long longWrapperTwo;

      @Property
      private String stringOne;

      @Property  (defaultValue="Hello World!")
      private String stringTwo;

      @Property (inherit=Inherit.SUPERCLASS_PROPERTY)
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