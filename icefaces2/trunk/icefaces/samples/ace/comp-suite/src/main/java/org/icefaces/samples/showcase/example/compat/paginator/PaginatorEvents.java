package org.icefaces.samples.showcase.example.compat.paginator;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.icesoft.faces.component.datapaginator.DataPaginator;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = PaginatorBean.BEAN_NAME,
        title = "example.compat.paginator.events.title",
        description = "example.compat.paginator.events.description",
        example = "/resources/examples/compat/paginator/paginatorEvents.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="paginatorEvents.xhtml",
                    resource = "/resources/examples/compat/"+
                               "paginator/paginatorEvents.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PaginatorEvents.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/paginator/PaginatorEvents.java")
        }
)
@ManagedBean(name= PaginatorEvents.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PaginatorEvents extends ComponentExampleImpl<PaginatorEvents> implements Serializable {
	
	public static final String BEAN_NAME = "paginatorEvents";
	
	private String status = "No events yet. Click the paginator to fire an event.";
	
	public PaginatorEvents() {
		super(PaginatorEvents.class);
	}
	
	public String getStatus() { return status; }
	
	public void setStatus(String status) { this.status = status; }
	
	public void actionListener(ActionEvent event) {
	    setStatus("Data Paginator clicked.");
	    
	    if ((event.getComponent() != null) &&
	        (event.getComponent() instanceof DataPaginator)) {
	        DataPaginator clicked = (DataPaginator)event.getComponent();
	        
	        StringBuilder sb = new StringBuilder(80);
	        sb.append("Data Paginator clicked. Current page is ");
	        sb.append(clicked.getPageIndex());
	        sb.append(" of ");
	        sb.append(clicked.getPageCount());
	        sb.append(" and a maximum of ");
	        sb.append(clicked.getPaginatorMaxPages());
	        sb.append(" pages will be displayed.");
	        setStatus(sb.toString());
        }
	}
}
