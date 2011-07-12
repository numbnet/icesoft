package org.icefaces.samples.showcase.example.compat.dataTable;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
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
        title = "example.compat.dataTable.group.title",
        description = "example.compat.dataTable.group.description",
        example = "/resources/examples/compat/dataTable/dataTableGroup.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableGroup.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dataTable/dataTableGroup.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableGroup.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dataTable/DataTableGroup.java")
        }
)
@ManagedBean(name= DataTableGroup.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableGroup extends ComponentExampleImpl<DataTableGroup> implements Serializable {
	
	public static final String BEAN_NAME = "dataTableGroup";
	
	private List<Car> carsData;
	
	public DataTableGroup() {
		super(DataTableGroup.class);
	}
	
	@PostConstruct
	private void init() {
	    carsData = new ArrayList<Car>(DataTableData.CARS.subList(0, 20));
	    DataTableSort.sort(DataTableSort.SORT_COLUMN_CHASSIS, carsData);
	}
	
	public List<Car> getCarsData() { return carsData; }
	
	public void setCarsData(List<Car> carsData) { this.carsData = carsData; }
}
