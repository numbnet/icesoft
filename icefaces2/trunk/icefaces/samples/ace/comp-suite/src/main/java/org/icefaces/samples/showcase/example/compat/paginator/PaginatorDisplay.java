package org.icefaces.samples.showcase.example.compat.paginator;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.example.compat.dataTable.DataTableData;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = PaginatorBean.BEAN_NAME,
        title = "example.compat.paginator.display.title",
        description = "example.compat.paginator.display.description",
        example = "/resources/examples/compat/paginator/paginatorDisplay.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="paginatorDisplay.xhtml",
                    resource = "/resources/examples/compat/"+
                               "paginator/paginatorDisplay.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PaginatorDisplay.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/paginator/PaginatorDisplay.java")
        }
)
@ManagedBean(name= PaginatorDisplay.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PaginatorDisplay extends ComponentExampleImpl<PaginatorDisplay> implements Serializable {
	
	public static final String BEAN_NAME = "paginatorDisplay";
	
	private int rows = DataTableData.CARS.size();
	private boolean enable = false;
	
	public PaginatorDisplay() {
		super(PaginatorDisplay.class);
	}
	
	public int getRows() { return rows; }
	public boolean getEnable() { return enable; }
	
	public void setRows(int rows) { this.rows = rows; }
	public void setEnable(boolean enable) { this.enable = enable; }
}
