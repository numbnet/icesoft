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
        title = "example.compat.menuPopup.icons.title",
        description = "example.compat.menuPopup.icons.description",
        example = "/resources/examples/compat/menuPopup/menuPopupIcons.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="menuPopupIcons.xhtml",
                    resource = "/resources/examples/compat/"+
                               "menuPopup/menuPopupIcons.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MenuPopupIcons.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/menuPopup/MenuPopupIcons.java")
        }
)
@ManagedBean(name= MenuPopupIcons.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuPopupIcons extends ComponentExampleImpl<MenuPopupIcons> implements Serializable {
	
	public static final String BEAN_NAME = "menuPopupIcons";
	
	private boolean hide = false;
	
	public MenuPopupIcons() {
		super(MenuPopupIcons.class);
	}
	
	public boolean getHide() { return hide; }
	
	public void setHide(boolean hide) { this.hide = hide; }
}
