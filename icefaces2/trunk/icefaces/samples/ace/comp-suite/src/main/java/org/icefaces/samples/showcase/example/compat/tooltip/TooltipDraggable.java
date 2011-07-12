package org.icefaces.samples.showcase.example.compat.tooltip;

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
        parent = TooltipBean.BEAN_NAME,
        title = "example.compat.tooltip.draggable.title",
        description = "example.compat.tooltip.draggable.description",
        example = "/resources/examples/compat/tooltip/tooltipDraggable.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tooltipDraggable.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tooltip/tooltipDraggable.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TooltipDraggable.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tooltip/TooltipDraggable.java")
        }
)
@ManagedBean(name= TooltipDraggable.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TooltipDraggable extends ComponentExampleImpl<TooltipDraggable> implements Serializable {
	
	public static final String BEAN_NAME = "tooltipDraggable";
	
	private boolean draggable = true;
	
	public TooltipDraggable() {
		super(TooltipDraggable.class);
	}
	
	public boolean getDraggable() { return draggable; }
	
	public void setDraggable(boolean draggable) { this.draggable = draggable; }
}
