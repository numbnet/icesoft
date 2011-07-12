package org.icefaces.samples.showcase.example.compat.paginator;

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
        parent = PaginatorBean.BEAN_NAME,
        title = "example.compat.paginator.max.title",
        description = "example.compat.paginator.max.description",
        example = "/resources/examples/compat/paginator/paginatorMax.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="paginatorMax.xhtml",
                    resource = "/resources/examples/compat/"+
                               "paginator/paginatorMax.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PaginatorMax.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/paginator/PaginatorMax.java")
        }
)
@ManagedBean(name= PaginatorMax.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PaginatorMax extends ComponentExampleImpl<PaginatorMax> implements Serializable {
	
	public static final String BEAN_NAME = "paginatorMax";
	
	private int rows = 3;
	private int pages = 2;
	
	public PaginatorMax() {
		super(PaginatorMax.class);
	}
	
	public int getRows() { return rows; }
	public int getPages() { return pages; }
	
	public void setRows(int rows) { this.rows = rows; }
	public void setPages(int pages) { this.pages = pages; }
}
