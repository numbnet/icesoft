package org.icefaces.samples.showcase.example.compat.exporter;

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
        parent = ExporterBean.BEAN_NAME,
        title = "example.compat.exporter.label.title",
        description = "example.compat.exporter.label.description",
        example = "/resources/examples/compat/exporter/exporterLabel.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="exporterLabel.xhtml",
                    resource = "/resources/examples/compat/"+
                               "exporter/exporterLabel.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ExporterLabel.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/exporter/ExporterLabel.java")
        }
)
@ManagedBean(name= ExporterLabel.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ExporterLabel extends ComponentExampleImpl<ExporterLabel> implements Serializable {
	
	public static final String BEAN_NAME = "exporterLabel";
	
	private String custom = "Download the Cars Data as a CSV File";
	
	public ExporterLabel() {
		super(ExporterLabel.class);
	}
	
	public String getCustom() { return custom; }
	
	public void setCustom(String custom) { this.custom = custom; }
}
