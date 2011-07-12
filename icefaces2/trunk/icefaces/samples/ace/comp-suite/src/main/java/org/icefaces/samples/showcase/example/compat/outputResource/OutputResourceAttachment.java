package org.icefaces.samples.showcase.example.compat.outputResource;

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
        parent = OutputResourceBean.BEAN_NAME,
        title = "example.compat.outputResource.attachment.title",
        description = "example.compat.outputResource.attachment.description",
        example = "/resources/examples/compat/outputResource/outputResourceAttachment.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="outputResourceAttachment.xhtml",
                    resource = "/resources/examples/compat/"+
                               "outputResource/outputResourceAttachment.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="OutputResourceAttachment.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/outputResource/OutputResourceAttachment.java")
        }
)
@ManagedBean(name= OutputResourceAttachment.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class OutputResourceAttachment extends ComponentExampleImpl<OutputResourceAttachment> implements Serializable {
	
	public static final String BEAN_NAME = "outputResourceAttachment";
	
	private boolean attachment = true;
	
	public OutputResourceAttachment() {
		super(OutputResourceAttachment.class);
	}
	
	public boolean getAttachment() { return attachment; }
	
	public void setAttachment(boolean attachment) { this.attachment = attachment; }
}
