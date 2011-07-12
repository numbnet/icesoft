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
        title = "example.compat.border.render.title",
        description = "example.compat.border.render.description",
        example = "/resources/examples/compat/border/borderRender.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="borderRender.xhtml",
                    resource = "/resources/examples/compat/"+
                               "border/borderRender.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="BorderRender.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/border/BorderRender.java")
        }
)
@ManagedBean(name= BorderRender.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class BorderRender extends ComponentExampleImpl<BorderRender> implements Serializable {
	
	public static final String BEAN_NAME = "borderRender";
	
	private boolean center = true;
	private boolean north = true;
	private boolean south = true;
	private boolean east = true;
	private boolean west = true;
	
	public BorderRender() {
		super(BorderRender.class);
	}
	
	public boolean getCenter() { return center; }
	public boolean getNorth() { return north; }
	public boolean getSouth() { return south; }
	public boolean getEast() { return east; }
	public boolean getWest() { return west; }
	
	public void setCenter(boolean center) { this.center = center; }
	public void setNorth(boolean north) { this.north = north; }
	public void setSouth(boolean south) { this.south = south; }
	public void setEast(boolean east) { this.east = east; }
	public void setWest(boolean west) { this.west = west; }
}
