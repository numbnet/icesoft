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
        title = "example.compat.tooltip.preview.title",
        description = "example.compat.tooltip.preview.description",
        example = "/resources/examples/compat/tooltip/tooltipPreview.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tooltipPreview.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tooltip/tooltipPreview.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TooltipPreview.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tooltip/TooltipPreview.java")
        }
)
@ManagedBean(name= TooltipPreview.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TooltipPreview extends ComponentExampleImpl<TooltipPreview> implements Serializable {
	
	public static final String BEAN_NAME = "tooltipPreview";
	
	public TooltipPreview() {
		super(TooltipPreview.class);
	}
}
