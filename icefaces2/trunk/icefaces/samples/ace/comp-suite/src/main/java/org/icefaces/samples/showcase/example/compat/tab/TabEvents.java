package org.icefaces.samples.showcase.example.compat.tab;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.icesoft.faces.component.paneltabset.TabChangeEvent;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = TabBean.BEAN_NAME,
        title = "example.compat.tab.events.title",
        description = "example.compat.tab.events.description",
        example = "/resources/examples/compat/tab/tabEvents.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tabEvents.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tab/tabEvents.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TabEvents.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tab/TabEvents.java")
        }
)
@ManagedBean(name= TabEvents.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TabEvents extends ComponentExampleImpl<TabEvents> implements Serializable {
	
	public static final String BEAN_NAME = "tabEvents";
	
	private String status = "No events fired yet.";
	
	public TabEvents() {
		super(TabEvents.class);
	}
	
	public String getStatus() { return status; }
	
	public void setStatus(String status) { this.status = status; }
	
	public void tabChangeListener(TabChangeEvent event) {
	    status = "Tab changed from index " + event.getOldTabIndex() + " to index " +
	             event.getNewTabIndex() + ".";
	}
}
