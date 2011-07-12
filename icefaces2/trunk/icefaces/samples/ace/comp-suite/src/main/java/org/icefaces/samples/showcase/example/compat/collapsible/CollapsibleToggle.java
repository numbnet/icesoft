package org.icefaces.samples.showcase.example.compat.collapsible;

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
        parent = CollapsibleBean.BEAN_NAME,
        title = "example.compat.collapsible.toggle.title",
        description = "example.compat.collapsible.toggle.description",
        example = "/resources/examples/compat/collapsible/collapsibleToggle.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="collapsibleToggle.xhtml",
                    resource = "/resources/examples/compat/"+
                               "collapsible/collapsibleToggle.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="CollapsibleToggle.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/collapsible/CollapsibleToggle.java")
        }
)
@ManagedBean(name= CollapsibleToggle.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CollapsibleToggle extends ComponentExampleImpl<CollapsibleToggle> implements Serializable {
	
	public static final String BEAN_NAME = "collapsibleToggle";
	
	private boolean toggle = true;
	
	public CollapsibleToggle() {
		super(CollapsibleToggle.class);
	}
	
	public boolean getToggle() { return toggle; }
	
	public void setToggle(boolean toggle) { this.toggle = toggle; }
}
