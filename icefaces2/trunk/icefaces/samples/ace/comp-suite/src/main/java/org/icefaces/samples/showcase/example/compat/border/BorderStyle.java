package org.icefaces.samples.showcase.example.compat.border;

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
        parent = BorderBean.BEAN_NAME,
        title = "example.compat.border.style.title",
        description = "example.compat.border.style.description",
        example = "/resources/examples/compat/border/borderStyle.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="borderStyle.xhtml",
                    resource = "/resources/examples/compat/"+
                               "border/borderStyle.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="BorderStyle.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/border/BorderStyle.java")
        }
)
@ManagedBean(name= BorderStyle.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class BorderStyle extends ComponentExampleImpl<BorderStyle> implements Serializable {
	
	public static final String BEAN_NAME = "borderStyle";
	
	private SelectItem[] availableStyles = new SelectItem[] {
	    new SelectItem("", "Default"),
	    new SelectItem("borderRed", "Red Background"),
	    new SelectItem("borderTall", "Large Height"),
	    new SelectItem("borderOutset", "Outset Border"),
	    new SelectItem("borderLeft", "Left Align")
	};
	private String style = availableStyles[0].getValue().toString();

	public BorderStyle() {
		super(BorderStyle.class);
	}
	
	public SelectItem[] getAvailableStyles() { return availableStyles; }
	public String getStyle() { return style; }
	
	public void setStyle(String style) { this.style = style; }
}
