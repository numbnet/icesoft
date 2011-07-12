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
        title = "example.compat.tooltip.delay.title",
        description = "example.compat.tooltip.delay.description",
        example = "/resources/examples/compat/tooltip/tooltipDelay.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tooltipDelay.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tooltip/tooltipDelay.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TooltipDelay.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tooltip/TooltipDelay.java")
        }
)
@ManagedBean(name= TooltipDelay.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TooltipDelay extends ComponentExampleImpl<TooltipDelay> implements Serializable {
	
	public static final String BEAN_NAME = "tooltipDelay";
	
	private int delay = 1000;
	
	public TooltipDelay() {
		super(TooltipDelay.class);
	}
	
	public int getDelay() { return delay; }
	
	public void setDelay(int delay) { this.delay = delay; }
}
