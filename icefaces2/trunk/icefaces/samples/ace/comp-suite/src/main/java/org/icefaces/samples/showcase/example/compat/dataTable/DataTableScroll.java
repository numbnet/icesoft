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
        title = "example.compat.dataTable.scroll.title",
        description = "example.compat.dataTable.scroll.description",
        example = "/resources/examples/compat/dataTable/dataTableScroll.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableScroll.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dataTable/dataTableScroll.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableScroll.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dataTable/DataTableScroll.java")
        }
)
@ManagedBean(name= DataTableScroll.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableScroll extends ComponentExampleImpl<DataTableScroll> implements Serializable {
	
	public static final String BEAN_NAME = "dataTableScroll";
	
	private static final int DEFAULT_HEIGHT = 100;
	
	private int height = DEFAULT_HEIGHT;
	
	public DataTableScroll() {
		super(DataTableScroll.class);
	}
	
	public int getHeight() { return height; }
	
	public void setHeight(int height) { this.height = height; }
}
