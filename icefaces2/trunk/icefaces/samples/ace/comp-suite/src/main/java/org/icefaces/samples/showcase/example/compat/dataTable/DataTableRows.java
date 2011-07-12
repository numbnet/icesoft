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
        title = "example.compat.dataTable.rows.title",
        description = "example.compat.dataTable.rows.description",
        example = "/resources/examples/compat/dataTable/dataTableRows.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableRows.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dataTable/dataTableRows.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableRows.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dataTable/DataTableRows.java")
        }
)
@ManagedBean(name= DataTableRows.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableRows extends ComponentExampleImpl<DataTableRows> implements Serializable {
	
	public static final String BEAN_NAME = "dataTableRows";
	
	private int rows = DataTableData.DEFAULT_ROWS;
	
	public DataTableRows() {
		super(DataTableRows.class);
	}
	
	public int getRows() { return rows; }
	
	public void setRows(int rows) { this.rows = rows; }
}
