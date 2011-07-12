package org.icefaces.samples.showcase.example.compat.dataTable;

import java.io.Serializable;
import java.util.Random;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.view.navigation.NavigationController;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.compat.dataTable.widths.title",
        description = "example.compat.dataTable.widths.description",
        example = "/resources/examples/compat/dataTable/dataTableWidths.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableWidths.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dataTable/dataTableWidths.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableWidths.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dataTable/DataTableWidths.java")
        }
)
@ManagedBean(name= DataTableWidths.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableWidths extends ComponentExampleImpl<DataTableWidths> implements Serializable {
	
	public static final String BEAN_NAME = "dataTableWidths";
	
	private static final int NUM_COLS = 8;
	private static final int DEFAULT_WIDTH = 55;
	private static final int WIDTH_INCREMENT = 10;
	private static final int MIN_WIDTH = 25;
	private static final int MAX_WIDTH = 300;
	
	private Random randomizer = new Random(System.nanoTime());
	private int currentWidth = DEFAULT_WIDTH;
	private String widthString = buildWidthString(currentWidth);
	
	public DataTableWidths() {
		super(DataTableWidths.class);
	}
	
	public String getWidthString() { return widthString; }
	
	public void setWidthString(String widthString) { this.widthString = widthString; }
	
	private void rebuildWidthString() {
	    widthString = buildWidthString(currentWidth);
	    
	    NavigationController.refreshPage();
	}
	
	private String buildWidthString(int width) {
	    StringBuilder toReturn = new StringBuilder(NUM_COLS * 5);
	    
	    for (int i = 0; i < NUM_COLS; i++) {
	        toReturn.append(width);
	        toReturn.append("px");
	        
	        if ((i+1) < NUM_COLS) {
	            toReturn.append(",");
	        }
	    }
	    
	    return toReturn.toString();
	}
	
	public void increaseWidth(ActionEvent event) {
	    currentWidth += WIDTH_INCREMENT;
	    
	    if (currentWidth > MAX_WIDTH) {
	        currentWidth = MAX_WIDTH;
	    }
	    
	    rebuildWidthString();
	}
	
	public void decreaseWidth(ActionEvent event) {
	    currentWidth -= WIDTH_INCREMENT;
	    
	    if (currentWidth < MIN_WIDTH) {
	        currentWidth = MIN_WIDTH;
	    }
	    
	    rebuildWidthString();
	}
	
	public void randomWidth(ActionEvent event) {
	    currentWidth = MIN_WIDTH+randomizer.nextInt((MAX_WIDTH-MIN_WIDTH));
	    
	    rebuildWidthString();
	}
	
	public void defaultWidth(ActionEvent event) {
	    currentWidth = DEFAULT_WIDTH;
	    
	    rebuildWidthString();
	}
}
