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
        title = "example.compat.menuPopup.separator.title",
        description = "example.compat.menuPopup.separator.description",
        example = "/resources/examples/compat/menuPopup/menuPopupSeparator.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="menuPopupSeparator.xhtml",
                    resource = "/resources/examples/compat/"+
                               "menuPopup/menuPopupSeparator.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MenuPopupSeparator.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/menuPopup/MenuPopupSeparator.java")
        }
)
@ManagedBean(name= MenuPopupSeparator.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuPopupSeparator extends ComponentExampleImpl<MenuPopupSeparator> implements Serializable {
	
	public static final String BEAN_NAME = "menuPopupSeparator";
	
	private boolean hideSeparators = false;
	
	public MenuPopupSeparator() {
		super(MenuPopupSeparator.class);
	}
	
	public boolean getHideSeparators() { return hideSeparators; }
	
	public void setHideSeparators(boolean hideSeparators) { this.hideSeparators = hideSeparators; }
}
