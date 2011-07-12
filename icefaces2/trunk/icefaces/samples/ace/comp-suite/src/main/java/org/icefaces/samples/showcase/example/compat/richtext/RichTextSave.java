package org.icefaces.samples.showcase.example.compat.richtext;

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
        parent = RichTextBean.BEAN_NAME,
        title = "example.compat.richtext.save.title",
        description = "example.compat.richtext.save.description",
        example = "/resources/examples/compat/richtext/richtextSave.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="richtextSave.xhtml",
                    resource = "/resources/examples/compat/"+
                               "richtext/richtextSave.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="RichTextSave.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/richtext/RichTextSave.java")
        }
)
@ManagedBean(name= RichTextSave.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class RichTextSave extends ComponentExampleImpl<RichTextSave> implements Serializable {
	
	public static final String BEAN_NAME = "richtextSave";
	
	private String text;
	private boolean saveEnabled = true;
	
	public RichTextSave() {
		super(RichTextSave.class);
	}
	
	public String getText() { return text; }
	public boolean getSaveEnabled() { return saveEnabled; }
	
	public void setText(String text) { this.text = text; }
	public void setSaveEnabled(boolean saveEnabled) { this.saveEnabled = saveEnabled; }
}
