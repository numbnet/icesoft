package org.icefaces.samples.showcase.example.compat.collapsible;

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
        parent = CollapsibleBean.BEAN_NAME,
        title = "example.compat.collapsible.multiple.title",
        description = "example.compat.collapsible.multiple.description",
        example = "/resources/examples/compat/collapsible/collapsibleMultiple.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="collapsibleMultiple.xhtml",
                    resource = "/resources/examples/compat/"+
                               "collapsible/collapsibleMultiple.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="CollapsibleMultiple.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/collapsible/CollapsibleMultiple.java")
        }
)
@ManagedBean(name= CollapsibleMultiple.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CollapsibleMultiple extends ComponentExampleImpl<CollapsibleMultiple> implements Serializable {
	
	public static final String BEAN_NAME = "collapsibleMultiple";
	
	public CollapsibleMultiple() {
		super(CollapsibleMultiple.class);
	}
}
