package org.icefaces.samples.showcase.example.compat.collapsible;

import java.io.Serializable;
import java.util.Random;

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
        title = "example.compat.collapsible.event.title",
        description = "example.compat.collapsible.event.description",
        example = "/resources/examples/compat/collapsible/collapsibleEvent.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="collapsibleEvent.xhtml",
                    resource = "/resources/examples/compat/"+
                               "collapsible/collapsibleEvent.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="CollapsibleEvent.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/collapsible/CollapsibleEvent.java")
        }
)
@ManagedBean(name= CollapsibleEvent.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CollapsibleEvent extends ComponentExampleImpl<CollapsibleEvent> implements Serializable {
	
	public static final String BEAN_NAME = "collapsibleEvent";
	
	private Random randomizer = new Random(System.nanoTime());
	private long lastFired;
	private int lastNumber;
	
	public CollapsibleEvent() {
		super(CollapsibleEvent.class);
	}
	
	public long getLastFired() { return lastFired; }
	public int getLastNumber() { return lastNumber; }
	
	public void setLastFired(long lastFired) { this.lastFired = lastFired; }
	public void setLastNumber(int lastNumber) { this.lastNumber = lastNumber; }
	
	public void actionListener(ActionEvent event) {
	    lastFired = System.currentTimeMillis();
	    lastNumber = 1+randomizer.nextInt(99);
	}
}
