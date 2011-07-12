package org.icefaces.samples.showcase.example.compat.dataTable;

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
        parent = DataTableBean.BEAN_NAME,
        title = "example.compat.dataTable.client.title",
        description = "example.compat.dataTable.client.description",
        example = "/resources/examples/compat/dataTable/dataTableClient.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableClient.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dataTable/dataTableClient.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableClient.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dataTable/DataTableClient.java")
        }
)
@ManagedBean(name= DataTableClient.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableClient extends ComponentExampleImpl<DataTableClient> implements Serializable {
	
	public static final String BEAN_NAME = "dataTableClient";
	
	private boolean enable = true;
	
	public DataTableClient() {
		super(DataTableClient.class);
	}
	
	public boolean getEnable() { return enable; }
	
	public void setEnable(boolean enable) { this.enable = enable; }
}
