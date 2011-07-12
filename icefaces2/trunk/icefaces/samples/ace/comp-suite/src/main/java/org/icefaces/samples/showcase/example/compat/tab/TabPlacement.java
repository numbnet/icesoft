package org.icefaces.samples.showcase.example.compat.tab;

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
        parent = TabBean.BEAN_NAME,
        title = "example.compat.tab.placement.title",
        description = "example.compat.tab.placement.description",
        example = "/resources/examples/compat/tab/tabPlacement.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tabPlacement.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tab/tabPlacement.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TabPlacement.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tab/TabPlacement.java")
        }
)
@ManagedBean(name= TabPlacement.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TabPlacement extends ComponentExampleImpl<TabPlacement> implements Serializable {
	
	public static final String BEAN_NAME = "tabPlacement";
	
	private String[] availablePlacements = new String[] {
	    "Bottom",
	    "Top"
	};
	private String placement = availablePlacements[0];
	
	public TabPlacement() {
		super(TabPlacement.class);
	}
	
	public String[] getAvailablePlacements() { return availablePlacements; }
	public String getPlacement() { return placement; }
	
	public void setPlacement(String placement) { this.placement = placement; }
}
