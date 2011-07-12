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
        title = "example.compat.richtext.toolbar.title",
        description = "example.compat.richtext.toolbar.description",
        example = "/resources/examples/compat/richtext/richtextToolbar.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="richtextToolbar.xhtml",
                    resource = "/resources/examples/compat/"+
                               "richtext/richtextToolbar.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="RichTextToolbar.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/richtext/RichTextToolbar.java")
        }
)
@ManagedBean(name= RichTextToolbar.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class RichTextToolbar extends ComponentExampleImpl<RichTextToolbar> implements Serializable {
	
	public static final String BEAN_NAME = "richtextToolbar";
	
	private String text;
	private SelectItem[] availableToolbars = new SelectItem[] {
	    new SelectItem("Basic", "Basic"),
	    new SelectItem("Default", "Rich")
	};
	private String toolbar = availableToolbars[0].getValue().toString();
	
	public RichTextToolbar() {
		super(RichTextToolbar.class);
	}
	
	public String getText() { return text; }
	public String getToolbar() { return toolbar; }
	public SelectItem[] getAvailableToolbars() { return availableToolbars; }
	
	public void setText(String text) { this.text = text; }
	public void setToolbar(String toolbar) { this.toolbar = toolbar; }
}
