  package org.icefaces.component.commandlink;

import javax.faces.component.UIComponent;
import javax.el.MethodExpression;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

  @Component(
        tagName ="linkButton",
          componentClass ="org.icefaces.component.commandlink.CommandLink",
          rendererClass ="org.icefaces.component.commandlink.CommandLinkRenderer",
          componentType = "org.icefaces.CommandLink",
          rendererType = "org.icefaces.CommandLinkRenderer",
          extendsClass = "javax.faces.component.UICommand",
          generatedClass = "org.icefaces.component.commandlink.CommandLinkBase",
          componentFamily="com.icesoft.faces.CommandLink"
          )

@ResourceDependencies({
	@ResourceDependency(library = "yui/2_8_1", name = "button/assets/skins/sam/button.css"),
    @ResourceDependency(library = "yui/2_8_1", name = "logger/assets/skins/sam/logger.css"),
    @ResourceDependency(library = "yui/2_8_1", name = "yahoo-dom-event/yahoo-dom-event.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "element/element-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "button/button-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "logger/logger-min.js"),
	@ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="commandlink.js",library="org.icefaces.component.commandlink")
})

  public class CommandLinkMeta {

      @Property
      private String label;

      @Property (inherit=true, useTemplate=true)
      private String id;

      @Property
      private String image;

      @Property
      private String href;

       @Property
      private String hrefLang;

      @Property
      private String name;

      @Property
      private String rel; 

      @Property
      private String rev;

      @Property
      private String shape;

      @Property(defaultValue="false",
              tlddoc="Default is false, means uses full submit")
      private Boolean singleSubmit;

      @Property (inherit=true, useTemplate=true)
      private UIComponent binding;

      @Property (defaultValue="false")
      private Boolean disabled;

      @Property (inherit=true, defaultValue="true")
      private Boolean rendered;

      @Property (defaultValue="0", tlddoc="tabindex of the component")
      private Integer tabindex;

      @Property(tlddoc="style class of the component, the renderer doesn't render any default class.")
      private String styleClass;

      @Property(tlddoc="style of the component")
      private String style;

      @Property(inherit=true, isMethodExpression=true)
    private MethodExpression actionListener;

    @Property(isMethodExpression=true, inherit=true	)
    private MethodExpression action;

    @Property(defaultValue="false",inherit=true, useTemplate=true)
    private Boolean immediate;
  }