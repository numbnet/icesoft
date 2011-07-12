package org.icefaces.samples.showcase.example.compat.menuBar;

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
        parent = MenuBarBean.BEAN_NAME,
        title = "example.compat.menuBar.click.title",
        description = "example.compat.menuBar.click.description",
        example = "/resources/examples/compat/menuBar/menuBarClick.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="menuBarClick.xhtml",
                    resource = "/resources/examples/compat/"+
                               "menuBar/menuBarClick.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MenuBarClick.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/menuBar/MenuBarClick.java")
        }
)
@ManagedBean(name= MenuBarClick.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuBarClick extends ComponentExampleImpl<MenuBarClick> implements Serializable {
	
	public static final String BEAN_NAME = "menuBarClick";
	
	private boolean displayOnClick = true;
	
	public MenuBarClick() {
		super(MenuBarClick.class);
	}
	
	public boolean getDisplayOnClick() { return displayOnClick; }
	
	public void setDisplayOnClick(boolean displayOnClick) { this.displayOnClick = displayOnClick; }
}
