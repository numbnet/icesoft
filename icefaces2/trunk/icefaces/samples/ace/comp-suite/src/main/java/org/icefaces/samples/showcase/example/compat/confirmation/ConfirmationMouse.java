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
        title = "example.compat.confirmation.mouse.title",
        description = "example.compat.confirmation.mouse.description",
        example = "/resources/examples/compat/confirmation/confirmationMouse.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="confirmationMouse.xhtml",
                    resource = "/resources/examples/compat/"+
                               "confirmation/confirmationMouse.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ConfirmationMouse.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/confirmation/ConfirmationMouse.java")
        }
)
@ManagedBean(name= ConfirmationMouse.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ConfirmationMouse extends ComponentExampleImpl<ConfirmationMouse> implements Serializable {
	
	public static final String BEAN_NAME = "confirmationMouse";
	
	private String text;
	private boolean displayAtMouse = true;
	
	public ConfirmationMouse() {
		super(ConfirmationMouse.class);
	}
	
	public String getText() { return text; }
	public boolean getDisplayAtMouse() { return displayAtMouse; }
	
	public void setText(String text) { this.text = text; }
	public void setDisplayAtMouse(boolean displayAtMouse) { this.displayAtMouse = displayAtMouse; }
	
	public void clearText(ActionEvent event) {
	    setText(null);
	}
}
