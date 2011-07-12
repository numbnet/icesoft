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
        title = "example.compat.exporter.pagination.title",
        description = "example.compat.exporter.pagination.description",
        example = "/resources/examples/compat/exporter/exporterPagination.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="exporterPagination.xhtml",
                    resource = "/resources/examples/compat/"+
                               "exporter/exporterPagination.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ExporterPagination.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/exporter/ExporterPagination.java")
        }
)
@ManagedBean(name= ExporterPagination.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ExporterPagination extends ComponentExampleImpl<ExporterPagination> implements Serializable {
	
	public static final String BEAN_NAME = "exporterPagination";
	
	private boolean ignore = true;
	
	public ExporterPagination() {
		super(ExporterPagination.class);
	}
	
	public boolean getIgnore() { return ignore; }
	
	public void setIgnore(boolean ignore) { this.ignore = ignore; }
}
