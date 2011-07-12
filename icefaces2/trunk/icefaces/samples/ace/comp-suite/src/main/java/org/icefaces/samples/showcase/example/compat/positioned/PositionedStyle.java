package org.icefaces.samples.showcase.example.compat.positioned;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = PositionedBean.BEAN_NAME,
        title = "example.compat.positioned.style.title",
        description = "example.compat.positioned.style.description",
        example = "/resources/examples/compat/positioned/positionedStyle.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="positionedStyle.xhtml",
                    resource = "/resources/examples/compat/"+
                               "positioned/positionedStyle.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PositionedStyle.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/positioned/PositionedStyle.java")
        }
)
@ManagedBean(name= PositionedStyle.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PositionedStyle extends ComponentExampleImpl<PositionedStyle> implements Serializable {
	
	public static final String BEAN_NAME = "positionedStyle";
	
	public PositionedStyle() {
		super(PositionedStyle.class);
	}
}
