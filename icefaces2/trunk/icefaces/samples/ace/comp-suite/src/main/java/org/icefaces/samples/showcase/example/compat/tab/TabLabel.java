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
        title = "example.compat.tab.label.title",
        description = "example.compat.tab.label.description",
        example = "/resources/examples/compat/tab/tabLabel.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tabLabel.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tab/tabLabel.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TabLabel.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tab/TabLabel.java")
        }
)
@ManagedBean(name= TabLabel.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TabLabel extends ComponentExampleImpl<TabLabel> implements Serializable {
	
	public static final String BEAN_NAME = "tabLabel";
	
	public TabLabel() {
		super(TabLabel.class);
	}
}
