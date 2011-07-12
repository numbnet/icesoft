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
        title = "example.compat.border.title",
        description = "example.compat.border.description",
        example = "/resources/examples/compat/border/border.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="border.xhtml",
                    resource = "/resources/examples/compat/"+
                               "border/border.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="BorderBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/border/BorderBean.java")
        }
)
@Menu(
	title = "menu.compat.border.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.border.subMenu.main",
                    isDefault = true,
                    exampleBeanName = BorderBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.border.subMenu.layout",
                    exampleBeanName = BorderLayout.BEAN_NAME),
            @MenuLink(title = "menu.compat.border.subMenu.render",
                    exampleBeanName = BorderRender.BEAN_NAME),
            @MenuLink(title = "menu.compat.border.subMenu.style",
                    exampleBeanName = BorderStyle.BEAN_NAME)
})
@ManagedBean(name= BorderBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class BorderBean extends ComponentExampleImpl<BorderBean> implements Serializable {
	
	public static final String BEAN_NAME = "border";
	
	public BorderBean() {
		super(BorderBean.class);
	}
}
