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
        title = "example.compat.tree.navigation.title",
        description = "example.compat.tree.navigation.description",
        example = "/resources/examples/compat/tree/treeNavigation.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="treeNavigation.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tree/treeNavigation.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TreeNavigation.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tree/TreeNavigation.java")
        }
)
@ManagedBean(name= TreeNavigation.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TreeNavigation extends ComponentExampleImpl<TreeNavigation> implements Serializable {
	
	public static final String BEAN_NAME = "treeNavigation";
	
	private boolean hideNavigation = true;
	
	public TreeNavigation() {
		super(TreeNavigation.class);
	}
	
	public boolean getHideNavigation() { return hideNavigation; }
	
	public void setHideNavigation(boolean hideNavigation) { this.hideNavigation = hideNavigation; }
	
	public void toggleNavigation(ActionEvent event) {
	    hideNavigation = !hideNavigation;
	}
}
