package org.icefaces.samples.showcase.example.compat.series;

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
        parent = SeriesBean.BEAN_NAME,
        title = "example.compat.series.rows.title",
        description = "example.compat.series.rows.description",
        example = "/resources/examples/compat/series/seriesRows.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="seriesRows.xhtml",
                    resource = "/resources/examples/compat/"+
                               "series/seriesRows.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="SeriesRows.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/series/SeriesRows.java")
        }
)
@ManagedBean(name= SeriesRows.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SeriesRows extends ComponentExampleImpl<SeriesRows> implements Serializable {
	
	public static final String BEAN_NAME = "seriesRows";
	public static final int DEFAULT_ROWS = 10;
	
	// Initially display less rows to show a difference from the other demos
	public int rows = DEFAULT_ROWS/3;
	
	public SeriesRows() {
		super(SeriesRows.class);
	}
	
	public int getRows() { return rows; }
	public int getDefaultRows() { return DEFAULT_ROWS; }
	public int getMaxRows() { return DEFAULT_ROWS; }
	
	public void setRows(int rows) { this.rows = rows; }
}
