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
        title = "example.compat.menuBar.icons.title",
        description = "example.compat.menuBar.icons.description",
        example = "/resources/examples/compat/menuBar/menuBarIcons.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="menuBarIcons.xhtml",
                    resource = "/resources/examples/compat/"+
                               "menuBar/menuBarIcons.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MenuBarIcons.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/menuBar/MenuBarIcons.java")
        }
)
@ManagedBean(name= MenuBarIcons.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuBarIcons extends ComponentExampleImpl<MenuBarIcons> implements Serializable {
	
	public static final String BEAN_NAME = "menuBarIcons";
	
	private boolean hide = true;
	
	public MenuBarIcons() {
		super(MenuBarIcons.class);
	}
	
	public boolean getHide() { return hide; }
	
	public void setHide(boolean hide) { this.hide = hide; }
}
