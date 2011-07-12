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
        title = "example.compat.series.first.title",
        description = "example.compat.series.first.description",
        example = "/resources/examples/compat/series/seriesFirst.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="seriesFirst.xhtml",
                    resource = "/resources/examples/compat/"+
                               "series/seriesFirst.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="SeriesFirst.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/series/SeriesFirst.java")
        }
)
@ManagedBean(name= SeriesFirst.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SeriesFirst extends ComponentExampleImpl<SeriesFirst> implements Serializable {
	
	public static final String BEAN_NAME = "seriesFirst";
	
	private int first = 5;
	
	public SeriesFirst() {
		super(SeriesFirst.class);
	}
	
	public int getFirst() { return first; }
	
	public void setFirst(int first) { this.first = first; }
}
