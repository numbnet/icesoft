package org.icefaces.samples.showcase.example.compat.confirmation;

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
        parent = ConfirmationBean.BEAN_NAME,
        title = "example.compat.confirmation.display.title",
        description = "example.compat.confirmation.display.description",
        example = "/resources/examples/compat/confirmation/confirmationDisplay.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="confirmationDisplay.xhtml",
                    resource = "/resources/examples/compat/"+
                               "confirmation/confirmationDisplay.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ConfirmationDisplay.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/confirmation/ConfirmationDisplay.java")
        }
)
@ManagedBean(name= ConfirmationDisplay.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ConfirmationDisplay extends ComponentExampleImpl<ConfirmationDisplay> implements Serializable {
	
	public static final String BEAN_NAME = "confirmationDisplay";
	
	private String text;
	private boolean autoCentre = true;
	private boolean draggable = true;
	
	public ConfirmationDisplay() {
		super(ConfirmationDisplay.class);
	}
	
	public String getText() { return text; }
	public boolean getAutoCentre() { return autoCentre; }
	public boolean getDraggable() { return draggable; }
	
	public void setText(String text) { this.text = text; }
	public void setAutoCentre(boolean autoCentre) { this.autoCentre = autoCentre; }
	public void setDraggable(boolean draggable) { this.draggable = draggable; }
	
	public void clearText(ActionEvent event) {
	    setText(null);
	}
}
