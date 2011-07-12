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
        title = "example.compat.menuBar.title",
        description = "example.compat.menuBar.description",
        example = "/resources/examples/compat/menuBar/menuBar.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="menuBar.xhtml",
                    resource = "/resources/examples/compat/"+
                               "menuBar/menuBar.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MenuBarBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/menuBar/MenuBarBean.java")
        }
)
@Menu(
	title = "menu.compat.menuBar.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.menuBar.subMenu.main",
                    isDefault = true,
                    exampleBeanName = MenuBarBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuBar.subMenu.events",
                    exampleBeanName = MenuBarEvents.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuBar.subMenu.orientation",
                    exampleBeanName = MenuBarOrientation.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuBar.subMenu.click",
                    exampleBeanName = MenuBarClick.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuBar.subMenu.keyboard",
                    exampleBeanName = MenuBarKeyboard.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuBar.subMenu.icons",
                    exampleBeanName = MenuBarIcons.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuBar.subMenu.separator",
                    exampleBeanName = MenuBarSeparator.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuBar.subMenu.dynamic",
                    exampleBeanName = MenuBarDynamic.BEAN_NAME)
})
@ManagedBean(name= MenuBarBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuBarBean extends ComponentExampleImpl<MenuBarBean> implements Serializable {
	
	public static final String BEAN_NAME = "menuBar";
	
	public MenuBarBean() {
		super(MenuBarBean.class);
	}
}
