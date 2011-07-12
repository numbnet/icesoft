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
        title = "example.compat.tooltip.move.title",
        description = "example.compat.tooltip.move.description",
        example = "/resources/examples/compat/tooltip/tooltipMove.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tooltipMove.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tooltip/tooltipMove.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TooltipMove.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tooltip/TooltipMove.java")
        }
)
@ManagedBean(name= TooltipMove.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TooltipMove extends ComponentExampleImpl<TooltipMove> implements Serializable {
	
	public static final String BEAN_NAME = "tooltipMove";
	
	private boolean move = true;
	
	public TooltipMove() {
		super(TooltipMove.class);
	}
	
	public boolean getMove() { return move; }
	
	public void setMove(boolean move) { this.move = move; }
}
