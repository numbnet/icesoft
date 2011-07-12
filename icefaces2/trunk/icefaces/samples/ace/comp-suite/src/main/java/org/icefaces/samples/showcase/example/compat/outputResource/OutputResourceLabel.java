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
        title = "example.compat.outputResource.label.title",
        description = "example.compat.outputResource.label.description",
        example = "/resources/examples/compat/outputResource/outputResourceLabel.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="outputResourceLabel.xhtml",
                    resource = "/resources/examples/compat/"+
                               "outputResource/outputResourceLabel.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="OutputResourceLabel.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/outputResource/OutputResourceLabel.java")
        }
)
@ManagedBean(name= OutputResourceLabel.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class OutputResourceLabel extends ComponentExampleImpl<OutputResourceLabel> implements Serializable {
	
	public static final String BEAN_NAME = "outputResourceLabel";
	
	private String label = "Custom Label";
	
	public OutputResourceLabel() {
		super(OutputResourceLabel.class);
	}
	
	public String getLabel() { return label; }
	
	public void setLabel(String label) { this.label = label; }
}
