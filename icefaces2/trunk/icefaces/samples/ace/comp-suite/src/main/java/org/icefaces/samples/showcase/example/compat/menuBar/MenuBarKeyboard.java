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
        title = "example.compat.menuBar.keyboard.title",
        description = "example.compat.menuBar.keyboard.description",
        example = "/resources/examples/compat/menuBar/menuBarKeyboard.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="menuBarKeyboard.xhtml",
                    resource = "/resources/examples/compat/"+
                               "menuBar/menuBarKeyboard.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MenuBarKeyboard.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/menuBar/MenuBarKeyboard.java")
        }
)
@ManagedBean(name= MenuBarKeyboard.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuBarKeyboard extends ComponentExampleImpl<MenuBarKeyboard> implements Serializable {
	
	public static final String BEAN_NAME = "menuBarKeyboard";
	
	private boolean enable = true;
	
	public MenuBarKeyboard() {
		super(MenuBarKeyboard.class);
	}
	
	public boolean getEnable() { return enable; }
	
	public void setEnable(boolean enable) { this.enable = enable; }
}
