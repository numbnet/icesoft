package org.icefaces.samples.showcase.example.compat.menuPopup;

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
        parent = MenuPopupBean.BEAN_NAME,
        title = "example.compat.menuPopup.keyboard.title",
        description = "example.compat.menuPopup.keyboard.description",
        example = "/resources/examples/compat/menuPopup/menuPopupKeyboard.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="menuPopupKeyboard.xhtml",
                    resource = "/resources/examples/compat/"+
                               "menuPopup/menuPopupKeyboard.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MenuPopupKeyboard.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/menuPopup/MenuPopupKeyboard.java")
        }
)
@ManagedBean(name= MenuPopupKeyboard.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuPopupKeyboard extends ComponentExampleImpl<MenuPopupKeyboard> implements Serializable {
	
	public static final String BEAN_NAME = "menuPopupKeyboard";
	
	private boolean enable = true;
	
	public MenuPopupKeyboard() {
		super(MenuPopupKeyboard.class);
	}
	
	public boolean getEnable() { return enable; }
	
	public void setEnable(boolean enable) { this.enable = enable; }
}
