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
        title = "example.compat.tab.wrapping.title",
        description = "example.compat.tab.wrapping.description",
        example = "/resources/examples/compat/tab/tabWrapping.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tabWrapping.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tab/tabWrapping.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TabWrapping.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tab/TabWrapping.java")
        }
)
@ManagedBean(name= TabWrapping.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TabWrapping extends ComponentExampleImpl<TabWrapping> implements Serializable {
	
	public static final String BEAN_NAME = "tabWrapping";
	
	public TabWrapping() {
		super(TabWrapping.class);
	}
}
