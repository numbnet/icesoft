package org.icefaces.samples.showcase.example.compat.tree;

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
        parent = TreeBean.BEAN_NAME,
        title = "example.compat.tree.icons.title",
        description = "example.compat.tree.icons.description",
        example = "/resources/examples/compat/tree/treeIcons.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="treeIcons.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tree/treeIcons.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TreeIcons.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tree/TreeIcons.java")
        }
)
@ManagedBean(name= TreeIcons.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TreeIcons extends ComponentExampleImpl<TreeIcons> implements Serializable {
	
	public static final String BEAN_NAME = "treeIcons";
	
	public TreeIcons() {
		super(TreeIcons.class);
	}
}
