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
        title = "example.compat.confirmation.label.title",
        description = "example.compat.confirmation.label.description",
        example = "/resources/examples/compat/confirmation/confirmationLabel.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="confirmationLabel.xhtml",
                    resource = "/resources/examples/compat/"+
                               "confirmation/confirmationLabel.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ConfirmationLabel.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/confirmation/ConfirmationLabel.java")
        }
)
@ManagedBean(name= ConfirmationLabel.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ConfirmationLabel extends ComponentExampleImpl<ConfirmationLabel> implements Serializable {
	
	public static final String BEAN_NAME = "confirmationLabel";
	
	private String text;
	private String acceptLabel = "Submit";
	private String cancelLabel = "Ignore";
	
	public ConfirmationLabel() {
		super(ConfirmationLabel.class);
	}
	
	public String getText() { return text; }
	public String getAcceptLabel() { return acceptLabel; }
	public String getCancelLabel() { return cancelLabel; }
	
	public void setText(String text) { this.text = text; }
	public void setAcceptLabel(String acceptLabel) { this.acceptLabel = acceptLabel; }
	public void setCancelLabel(String cancelLabel) { this.cancelLabel = cancelLabel; }
	
	public void clearText(ActionEvent event) {
	    setText(null);
	}
}
